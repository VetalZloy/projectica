package com.vetalzloy.projectica.web.websocket.endpoint;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

import com.vetalzloy.projectica.model.DialogMessage;
import com.vetalzloy.projectica.service.DialogService;
import com.vetalzloy.projectica.service.exception.UserNotFoundException;
import com.vetalzloy.projectica.util.SecurityUtil;
import com.vetalzloy.projectica.web.encoder.DialogMessagesEncoder;
import com.vetalzloy.projectica.web.message.DialogMessagesWrapper;
import com.vetalzloy.projectica.web.websocket.EndpointConfigurator;

/**
 * Deals with dialogs
 * @author VetalZloy
 *
 */
@ServerEndpoint(value="/dialogs/{username}", 
				configurator=EndpointConfigurator.class,
				encoders=DialogMessagesEncoder.class
				)
public class DialogEndpoint {
	
	private static final Logger logger = LoggerFactory.getLogger(DialogEndpoint.class);
	
	/**
	 * Represents all dialogs which are active now
	 */
	private static final Map<String, Set<Session>> dialogs = Collections.synchronizedMap(new HashMap<>());
	
	/**
	 * Config which has {@code Authentication} of current user
	 */
	private EndpointConfig config;	
	
	@Autowired
	private DialogService dialogMessageService;
	
	/**
	 * Opens connection for dialog
	 * @param session - current user's session
	 * @param config - config which will be initialized and put to SecurityUtil
	 * @param username - username of interlocutor
	 */
	@OnOpen
	public void onOpen(Session session, EndpointConfig config, @PathParam("username") String username){
		this.config = config;
		SecurityUtil.setAuthentication(config); //initialize Authentication
		
		session.getUserProperties().put("username", SecurityUtil.getCurrentUsername());
		
        String currentUsername = SecurityUtil.getCurrentUsername();
		logger.debug("User with username {} try to open session for messaging with user with username {} ...",
				currentUsername, username);

		String dialogId = null;
		try {
			dialogId = dialogMessageService.getDialogId(username);
		} catch (UserNotFoundException e) {
			logger.warn("Error happened during creating dialogId for dialog between "
					  + "users with usernames: '{}'-creator and '{}'",
					     currentUsername, username, e);
			return;
		}
		logger.debug("Dialog id : '{}'", dialogId);
		
		if(dialogs.get(dialogId) == null){
			logger.debug("Dialog with id '{}' doesn't exist => creating new one...", dialogId);
			Set<Session> dialog = Collections.synchronizedSet(new HashSet<>(2));
			dialog.add(session);
			dialogs.put(dialogId, dialog);
		} else {
			logger.debug("Dialog with id {} exists => adding new session to it.", dialogId);
			dialogs.get(dialogId).add(session);
		}
		
		logger.info("Session for messaging between users with usernames: creator: '{}' and '{}' was created successfully",
				currentUsername, username);
		
		//sending last messages to current user
		try {
			List<DialogMessage> list = dialogMessageService.getFirstPage(username);
			session.getBasicRemote().sendObject(new DialogMessagesWrapper(list));
		} catch (IOException | EncodeException | UserNotFoundException e) {
			logger.error("Error happened during sending messages within opening process between users with usernames: creator - {} and {}.",
					currentUsername, username, e);
		}
	}
	
	/**
	 * Closes connection for dialog
	 * @param session - current user's session
	 * @param username - username of interlocutor
	 */
	@OnClose
	public void onClose(Session session, @PathParam("username") String username){
		SecurityUtil.setAuthentication(config); //initialize Authentication
		String currentUsername = SecurityUtil.getCurrentUsername();
		
		logger.debug("User with username {} try to close session for messaging with user with username {} ...",
				currentUsername, username);
		
		String dialogId;
		try {
			dialogId = dialogMessageService.getDialogId(username);			
		} catch (UserNotFoundException e) {
			logger.warn("Error happened during extracting user.", e);
			return;
		}
		logger.debug("DialogId: '{}'", dialogId);
		
		boolean result = dialogs.get(dialogId).remove(session);
		if(result) 
			logger.info("Session for messaging between users with usernames: creator: '{}' and '{}' was closed successfully",
							currentUsername, username);
		else 
			logger.warn("Session for messaging between users with usernames: creator: '{}' and '{}' is not found.", 
							currentUsername, username);
	}
	
	@OnError
	public void onError(Throwable t,  @PathParam("username") String username){
		SecurityUtil.setAuthentication(config); //initialize Authentication
		String currentUsername = SecurityUtil.getCurrentUsername();
		logger.error("Error happened in messaging between users with usernames: creator: {} and {}",
				currentUsername, username, t);
	}
	
	/**
	 * Handle message
	 * @param session - current users's session
	 * @param text - message text
	 * @param recieverUsername - username of interlocutor (reviever)
	 */
	@OnMessage
	public void onMessage(Session session, String text, @PathParam("username") String recieverUsername){
		SecurityUtil.setAuthentication(config); //initialize Authentication
		
		String senderUsername = SecurityUtil.getCurrentUsername();
		logger.debug("User with username {} sended message to user with username {}",
				senderUsername, recieverUsername);

		String dialogId;
		DialogMessage message;
		try {
			dialogId = dialogMessageService.getDialogId(recieverUsername);
			logger.debug("DialogId: '{}'", dialogId);
			
			message = dialogMessageService.create(text, recieverUsername);
		} catch (UserNotFoundException e) {
			logger.warn("Error happened during extracting user.", e);
			return;
		}
				
		Set<Session> dialog = dialogs.get(dialogId);
		for(Session ses: dialog){
			try {
				ses.getBasicRemote().sendObject(new DialogMessagesWrapper(message));
			} catch (IOException | EncodeException e) {
				logger.error("Error happened during messaging between users with usernames: creator: {} and {}.",
						senderUsername, recieverUsername, e);
			}
		}
		
		/* 
		 * one user can open several tabs in browser 
		 * so we should remove duplicates to find out 
		 * whether both of interlocutors got new message
		 */
		long dialogMembers = dialog.stream()
								   .map(s -> s.getUserProperties().get("username"))
								   .distinct()
								   .count();
		if(dialogMembers == 2){
			//if message was sent to both interlocutors => was read by both of them
			dialogMessageService.putRead(message);
		}
	}
}
