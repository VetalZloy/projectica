package com.vetalzloy.projectica.web.controller;

import java.util.List;

import javax.websocket.EncodeException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vetalzloy.projectica.model.ChatMessage;
import com.vetalzloy.projectica.service.ChatService;
import com.vetalzloy.projectica.service.exception.AccessDeniedException;
import com.vetalzloy.projectica.service.exception.ChatRoomAlreadyExistsException;
import com.vetalzloy.projectica.service.exception.ChatRoomNotFoundException;
import com.vetalzloy.projectica.service.exception.ProjectNotFoundException;
import com.vetalzloy.projectica.util.SecurityUtil;
import com.vetalzloy.projectica.web.encoder.ChatMessagesEncoder;
import com.vetalzloy.projectica.web.json.CreateChatRoomJson;
import com.vetalzloy.projectica.web.message.ChatMessagesWrapper;

/**
 * Controller class which deal with "/chatrooms" requests
 * @author VetalZloy
 *
 */
@Controller
@RequestMapping("/chatrooms")
public class ChatRoomController {
	
	private static final Logger logger = LoggerFactory.getLogger(ChatRoomController.class);
	
	@Autowired
	private ChatService chatService;
	
	@Autowired
	private ChatMessagesEncoder encoder;
	
	/**
	 * Retrieves and fills model for chatRoom with id {@code id} 
	 * and returns name of .jsp file
	 * @param id - id of necessary chatRoom
	 * @param model - instance of {@code Model} class, in which we set necessary attributes
	 * @return "error" - if chatroom doesn't exist or user doesn't have access to this chatroom,
	 * else - "chatroom"
	 */
	@RequestMapping(path="/{id}", method=RequestMethod.GET, produces="text/html")
	public String getById(@PathVariable("id") int id, Model model){
		String currentUsername = SecurityUtil.getCurrentUsername();
		logger.debug("getById() method was invoked for user with username '{}'; requested chatroom id = {}", 
						currentUsername, id);		
		try {
			model.addAttribute("room", chatService.getById(id));			
			return "chatroom";
		} catch (ChatRoomNotFoundException | AccessDeniedException e) {
			logger.warn("Error happened during getting chat room by id.", e);
			return "error";
		}		
	}
	
	/**
	 * Returns page of chat messages which have ids lower than {@code earliestId} 
	 * from chatroom with such {@code id}, transforms list to json and returns it
	 * @param chatroomId - id of necessary chatroom
	 * @param earliestId - id which must be higher than all ids in retrieved messages 
	 * and must be the closest to them.
	 * @return JSON in form of String and OK http status, if all was done successfully
	 * or BAD_REQUEST http status, if something went wrong
	 */
	@ResponseBody
	@RequestMapping(path="/{id}", method=RequestMethod.GET, produces="application/json")
	public ResponseEntity<String> getBeforeEarliest(@PathVariable("id") int chatroomId, 
													@RequestParam("earliestId") long earliestId){
		String currentUsername = SecurityUtil.getCurrentUsername();
		logger.debug("getBeforeEarliest() method was invoked for chatroom with id = {} by user with username '{}'; ealiest message id = {}",
						chatroomId, currentUsername, earliestId);
		
		try {
			List<ChatMessage> list = chatService.getPageBeforeEarliest(earliestId, chatroomId);
			String json = encoder.encode(new ChatMessagesWrapper(list));
			return new ResponseEntity<String>(json, HttpStatus.OK);
		} catch (ChatRoomNotFoundException | AccessDeniedException | EncodeException e) {
			logger.warn("Error happened during retrieving chat messages from chat with id = {} before earliest messageId = {}",
							chatroomId, earliestId, e);
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}		
	}
	
	/**
	 * Create chatroom
	 * @param body - JSON transformed to {@code CreateChatRoomJson} instance
	 * @return BAD_REQUEST if something went wrong, else - OK
	 */
	@ResponseBody
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<Void> createChatRoom(@RequestBody CreateChatRoomJson body){
		String currentUsername = SecurityUtil.getCurrentUsername();
		logger.debug("createChatRoom() method was invoked by user with username '{}' for project with id = {}; desirable chatroom name = '{}'",
				currentUsername, body.getProjectId(), body.getName());
		
		try {
			chatService.createChatRoom(body.getProjectId(), body.getName());
			return new ResponseEntity<Void>(HttpStatus.OK);
		} catch (AccessDeniedException | ProjectNotFoundException | ChatRoomAlreadyExistsException e) {
			logger.warn("Error happened during creating chatroom with name = '{}' for project with id = {}, by user '{}'.",
					body.getName(), body.getProjectId(), currentUsername, e);
			return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
		}
		
	}
}
