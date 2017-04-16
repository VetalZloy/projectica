package com.vetalzloy.projectica.web.websocket.endpoint;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.websocket.EncodeException;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.vetalzloy.projectica.model.ChatMessage;
import com.vetalzloy.projectica.service.ChatService;
import com.vetalzloy.projectica.service.UserService;
import com.vetalzloy.projectica.service.exception.AccessDeniedException;
import com.vetalzloy.projectica.service.exception.ChatRoomNotFoundException;
import com.vetalzloy.projectica.service.exception.EntityNotFoundException;
import com.vetalzloy.projectica.service.exception.UserNotFoundException;
import com.vetalzloy.projectica.util.SecurityUtil;
import com.vetalzloy.projectica.web.encoder.ChatMessagesEncoder;
import com.vetalzloy.projectica.web.encoder.UsersMessageEncoder;
import com.vetalzloy.projectica.web.json.UserJson;
import com.vetalzloy.projectica.web.message.ChatMessagesWrapper;
import com.vetalzloy.projectica.web.message.UsersMessage;
import com.vetalzloy.projectica.web.websocket.EndpointConfigurator;

/**
 * Deals with chats
 * @author VetalZloy
 *
 */
@ServerEndpoint(value="/chatrooms/{id}", 
				configurator=EndpointConfigurator.class, 
				encoders={
						ChatMessagesEncoder.class,
						UsersMessageEncoder.class
				})
public class ChatEndpoint {
	
	private static final Logger logger = LoggerFactory.getLogger(ChatEndpoint.class);
	
	/**
	 * Represents all chat rooms which are active now
	 */
	private static final Map<Integer, Set<Session>> chatRooms = new HashMap<>();
	
	/**
	 * Config which has {@code Authentication} of current user
	 */
	private EndpointConfig config;
	
	@Autowired
	private ChatService chatService;
	
	@Autowired
	private UserService userService;
	
	/**
	 * Opens connection for chatting 
	 * @param session - current user's session
	 * @param config - config which will be initialized and put to SecurityUtil 
	 * to define current user from all layers of application
	 * @param id - id of necessary chat room
	 */
	@OnOpen
	public void onOpen(Session session, EndpointConfig config, @PathParam("id") int id){
		this.config = config;
		SecurityUtil.setAuthentication(config); //initialize Authentication
		
        String currentUsername = SecurityUtil.getCurrentUsername();
		logger.debug("User with username {} try to open session for chatting in chat with id = {} ...",
				currentUsername, id);
		
		//checking such chat room existence
		try {
			chatService.getById(id);
		} catch (ChatRoomNotFoundException | AccessDeniedException e) {
			logger.warn("Error happened during checking user with username '{}' access rights "
					  + "and chatRoom with id = {} existence.", 
					     currentUsername, id, e);
			return;
		}
		
		//putting JSON represents current user into session related with him
		try {
			UserJson userJson = userService.getCurrentUserJson();
			session.getUserProperties().put("userJson", userJson);
		} catch (UserNotFoundException e) {
			logger.warn("Error happened during creating user's data.", e);
			return;
		}
		
		//Adding current session into appropriate chat room (here chat room is Set of Sessions)
		if(chatRooms.get(id) == null){
			Set<Session> set = new HashSet<>();
			set.add(session);
			chatRooms.put(id, set);
		} else {
			chatRooms.get(id).add(session);
		}
		
		//Retrieves and sends to current user last chat message
		try {
			List<ChatMessage> firstPage = chatService.getFirstPage(id);
			session.getBasicRemote().sendObject(new ChatMessagesWrapper(firstPage));
		} catch (ChatRoomNotFoundException | AccessDeniedException | IOException | EncodeException e) {
			logger.warn("Error happened during sending first page from chatroom with id = {} to user with username '{}'",
					id, currentUsername, e);
			return;
		}
		
		sendUsersList(id);
	}

	/**
	 * Closes connection for chatting
	 * @param session - current user's session
	 * @param id - id of necessary chat room
	 */
	@OnClose
	public void onClose(Session session, @PathParam("id") int id){		
		SecurityUtil.setAuthentication(config); //initialize Authentication
		String currentUsername = SecurityUtil.getCurrentUsername();
		logger.debug("User with username {} try to close session for chatting in chat with id = {} ...",
				currentUsername, id);
		
		
		boolean result = chatRooms.get(id).remove(session);
		if(result) 
			logger.info("Session for chatting in chat with id = {} was closed successfully by user with username '{}'",
							id, currentUsername);
		else logger.warn("Session for chatting in chat with id = {} for user with username '{}' was bot found.",
							id, currentUsername);
		
		sendUsersList(id);
	}
	
	@OnError
	public void onError(Throwable t,  @PathParam("id") int id){
		SecurityUtil.setAuthentication(config); //initialize Authentication
		
		String currentUsername = SecurityUtil.getCurrentUsername();
		logger.error("Error happened in chatRoom with id = {}; username - '{}'",
				id, currentUsername, t);
	}
	
	/**
	 * Handle message data
	 * @param session - user's session
	 * @param text - text of message
	 * @param id - chat room id
	 */
	@OnMessage
	public void onMessage(Session session, String text, @PathParam("id") int id){
		SecurityUtil.setAuthentication(config); //initialize Authentication
		String currentUsername = SecurityUtil.getCurrentUsername();
		logger.debug("User with username {} trying to send message to chat with id {}",
				currentUsername, id);
		
		try {
			ChatMessage message = chatService.createMessage(id, text);
			for(Session s: chatRooms.get(id))
				s.getBasicRemote().sendObject(new ChatMessagesWrapper(message));
			
		} catch (EntityNotFoundException | AccessDeniedException | IOException | EncodeException e) {
			logger.error("Error happened during chatMessage creating by user with username = '{}' in chat with id = {}",
							currentUsername, id);
		}
		
		logger.info("Message, which was recieved from user with username '{}' for chat with id = {}, was created and sended successfully",
						SecurityUtil.getCurrentUsername(), id);
	}

	/**
	 * Sends list of users which are in chat room now
	 * @param id
	 */
	private void sendUsersList(int id) {
		
		Set<UserJson> users = chatRooms.get(id)
				  					.stream()
				  					.map(s -> (UserJson) s.getUserProperties().get("userJson"))
				  					.collect(Collectors.toSet());
		UsersMessage message = new UsersMessage(users);
		for(Session session: chatRooms.get(id))
			try {
				session.getBasicRemote().sendObject(message);
			} catch (IOException | EncodeException e) {
				logger.warn("Error happened during sending users list to user '{}' in chatroom with id = {}", 
						session.getUserProperties().get("user"), id, e);
			}
		
		
	}
	
}
