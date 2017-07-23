package com.vetalzloy.projectica.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.vetalzloy.projectica.service.UserService;
import com.vetalzloy.projectica.service.exception.UserNotFoundException;
import com.vetalzloy.projectica.util.SecurityUtil;

@Controller
@RequestMapping("/messages")
public class MessagesController {
	
private static final Logger logger = LoggerFactory.getLogger(MessagesController.class);
	
	@Autowired
	private UserService userService;
	
	/**
	 * Checks whether current user exists and returns name of template file will be displayed
	 * @return "error" - if current user doesn't registered, else "dialogs"
	 */
	@RequestMapping(path="", method=RequestMethod.GET, produces="text/html")
	public String dialogs(){
		logger.debug("dialogs() method was invoked by user with username '{}'",
						SecurityUtil.getCurrentUsername());		
		try {
			userService.getByUsername(SecurityUtil.getCurrentUsername());
			return "messages";
		} catch (UserNotFoundException e) {
			logger.warn("Error happened during checking user existance.", e);
			return "redirect:/?login";
		}
	}
	
}
