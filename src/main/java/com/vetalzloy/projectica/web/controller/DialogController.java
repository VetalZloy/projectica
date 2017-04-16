package com.vetalzloy.projectica.web.controller;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.websocket.EncodeException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vetalzloy.projectica.model.DialogMessage;
import com.vetalzloy.projectica.service.DialogService;
import com.vetalzloy.projectica.service.UserService;
import com.vetalzloy.projectica.service.exception.UserNotFoundException;
import com.vetalzloy.projectica.util.SecurityUtil;
import com.vetalzloy.projectica.web.encoder.DialogMessagesEncoder;
import com.vetalzloy.projectica.web.json.InterlocutorJson;
import com.vetalzloy.projectica.web.message.DialogMessagesWrapper;

/**
 * Controller class which deal with "/dialogs" requests
 * @author VetalZloy
 *
 */
@Controller
@RequestMapping("/dialogs")
public class DialogController {
	
	private static final Logger logger = LoggerFactory.getLogger(DialogController.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private DialogService dialogService;
	
	@Autowired
	private DialogMessagesEncoder encoder;
	
	/**
	 * Retrieves all dialogs for current user and fill {@code model}.
	 * Returns name of .jsp file will be displayed
	 * @param model - {@code Model} instance which will be filled
	 * @return "error" - if current user doesn't registered, else "dialogs"
	 */
	@RequestMapping(path="", method=RequestMethod.GET, produces="text/html")
	public String dialogs(Model model){
		logger.debug("dialogs() method was invoked by user with username '{}'",
						SecurityUtil.getCurrentUsername());		
		try {
			List<InterlocutorJson> interlocutors = userService.getInterlocutors()
															  .stream()
															  .map(i -> InterlocutorJson.create(i))
															  .collect(Collectors.toList());
			Collections.sort(interlocutors);
			model.addAttribute("interlocutors", interlocutors);
			return "dialogs";
		} catch (UserNotFoundException e) {
			logger.warn("Error happened during retrieving interlocutors for user with username.", e);
			return "error";
		}
	}
	
	/**
	 * Returns amount of unread messages for current user
	 * @return real amount and OK - if all went right,
	 * else -1 and BAD_REQUEST
	 */
	@ResponseBody
	@RequestMapping(path="", method=RequestMethod.GET, produces="application/json")
	public ResponseEntity<Integer> getUnreadMessagesAmount(){
		
		// few logs because of frequency of invoking this method
		try {
			int unreadAmount = dialogService.getUnreadMessagesAmount();
			return new ResponseEntity<Integer>(unreadAmount, HttpStatus.OK);
		} catch (UserNotFoundException e) {
			logger.warn("Unregistered user '{}' tried to get amount of unread dialog messages", 
						SecurityUtil.getCurrentUsername());			
			return new ResponseEntity<Integer>(-1, HttpStatus.BAD_REQUEST);
		}
	}
	
	/**
	 * Retrieve data about dialog between current user and user with such {@code username} and
	 * returns name of .jsp file will be displayed
	 * @param username - username of interlocutor of current user
	 * @param model - {@code Model} instance will be filled
	 * @return "error" if current user or necessary interlocutor doesn't exist, 
	 * else "dialog"
	 */
	@RequestMapping(path="/{username}", produces="text/html")
	public String dialog(@PathVariable("username") String username, Model model){
		String currentUsername = SecurityUtil.getCurrentUsername();
		logger.debug("dialog() method was invoked by user with username {} for messaging with user with username {}",
				currentUsername, username);
		
		if("anonymousUser".equals(currentUsername)) {
			logger.warn("Unauthorized user tries to open dialog page with user '{}'", username);
			return "redirect:/";
		}
		
		try {
			model.addAttribute("user", userService.getByUsername(username));			
			return "dialog";
		} catch (UserNotFoundException e) {
			logger.warn("Error happened during extracting user.", e);
			return "error";		
		}
	}
	
	/**
	 * Returns page of dialog messages which have ids lower than {@code earliestId} 
	 * from dialog between current user and user with username {@code interlocutorUsername}
	 * in form of JSON
	 * @param interlocutor - username of interlocutor of current user
	 * @param earliestId - id which must be higher than all ids in retrieved messages 
	 * and must be the closest to them.
	 * @return OK and JSON in String form - if all went right,<br>
	 * NOT_FOUND - if current user or interlocutor doesn't registered,<br>
	 * BAD_REQUEST - if error happened during encoding (mb another status should be thrown, IDK)
	 */
	@ResponseBody
	@RequestMapping(path="/{interlocutor}", produces="application/json")
	public ResponseEntity<String> getPageBeforeEarliest(@PathVariable("interlocutor") String interlocutor, 
												  		@RequestParam("earliestId") long earliestId){
		String currentUsername = SecurityUtil.getCurrentUsername();
		
		logger.debug("getPageBeforeEarliest() method was invoked for dialog between users with usernames: "
				+ "current -'{}' and '{}'; earliest messageId = {}.", 
				currentUsername, interlocutor, earliestId);
				
		try {
			List<DialogMessage> list = dialogService.getPageBeforeEarliest(earliestId,
																				  interlocutor);
			String json = encoder.encode(new DialogMessagesWrapper(list));
			return new ResponseEntity<>(json, HttpStatus.OK);
		} catch (UserNotFoundException e) {
			logger.warn("Error happened during extracting user.", e);
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		} catch (EncodeException e) {
			logger.warn("Error happened during messages encoding.", e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
}
