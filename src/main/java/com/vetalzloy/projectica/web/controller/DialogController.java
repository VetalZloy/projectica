package com.vetalzloy.projectica.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.vetalzloy.projectica.service.UserService;
import com.vetalzloy.projectica.service.exception.UserNotFoundException;
import com.vetalzloy.projectica.util.SecurityUtil;

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
	
	/**
	 * Retreive data about dialog between current user and user with such {@code username} and
	 * returns name of template file will be displayed
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
			return "redirect:/?login";
		}
		
		try {
			
			model.addAttribute("interlocutor", userService.getByUsername(username));	
			model.addAttribute("user", userService.getByUsername(currentUsername));
			
			return "dialog";
		} catch (UserNotFoundException e) {
			logger.warn("Error happened during extracting user.", e);
			return "error";		
		}
	}
	
}
