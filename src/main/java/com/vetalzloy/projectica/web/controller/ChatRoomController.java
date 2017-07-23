package com.vetalzloy.projectica.web.controller;

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
import org.springframework.web.bind.annotation.ResponseBody;

import com.vetalzloy.projectica.model.ChatRoom;
import com.vetalzloy.projectica.service.ChatService;
import com.vetalzloy.projectica.service.exception.AccessDeniedException;
import com.vetalzloy.projectica.service.exception.ChatRoomAlreadyExistsException;
import com.vetalzloy.projectica.service.exception.ChatRoomNotFoundException;
import com.vetalzloy.projectica.service.exception.ExternalResourceAccessException;
import com.vetalzloy.projectica.service.exception.ProjectNotFoundException;
import com.vetalzloy.projectica.util.SecurityUtil;
import com.vetalzloy.projectica.web.json.ChatRoomJson;
import com.vetalzloy.projectica.web.json.CreateChatRoomJson;

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
	
	/**
	 * Retreives and fills model for chatRoom with id {@code id} 
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
	 * Retreives chatroom with such {@code id} and convert it to JSON
	 * @param id - id of necessary chatRoom
	 * @return "error" - if chatroom doesn't exist or user doesn't have access to this chatroom,
	 * else - JSON representation of necessary chatroom
	 */
	@ResponseBody
	@RequestMapping(path="/{id}", method=RequestMethod.GET, produces="application/json")
	public ResponseEntity<ChatRoomJson> getById(@PathVariable("id") int id){
		String currentUsername = SecurityUtil.getCurrentUsername();
		logger.debug("JSON getById() method was invoked for user with username '{}'; requested chatroom id = {}", 
						currentUsername, id);		
		try {
			ChatRoom room = chatService.getById(id);			
			return new ResponseEntity<>(ChatRoomJson.create(room), HttpStatus.OK);
		} catch (ChatRoomNotFoundException | AccessDeniedException e) {
			logger.warn("Error happened during getting chat room by id.", e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}		
	}
	
	/**
	 * Creates chatroom
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
		} catch (AccessDeniedException | ProjectNotFoundException | ChatRoomAlreadyExistsException | ExternalResourceAccessException e) {
			logger.warn("Error happened during creating chatroom with name = '{}' for project with id = {}, by user '{}'.",
					body.getName(), body.getProjectId(), currentUsername, e);
			return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
		}
		
	}
}
