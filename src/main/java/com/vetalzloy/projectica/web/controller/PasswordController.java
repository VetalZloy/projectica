package com.vetalzloy.projectica.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.vetalzloy.projectica.service.UserService;
import com.vetalzloy.projectica.service.exception.PasswordTokenNotFoundException;
import com.vetalzloy.projectica.service.exception.UserNotFoundException;

/**
 * This controller deals with password changing
 * @author VetalZloy
 *
 */
@Controller
@RequestMapping("/*password*")
public class PasswordController {
	
	private static final Logger logger = LoggerFactory.getLogger(PasswordController.class);
	
	private static final String PASSWORD_PATTERN = "([A-Za-z]|\\d){5,}";
	
	@Autowired
	private UserService userService;
	
	/**
	 * Creates password token, returns name of .jsp file will be displayed
	 * @param username - username of user who forget password
	 * @param model - {@code Model} instance will be filled
	 * @return "error" - if user with such {@code username} doesn't registered,
	 * else "info"
	 */
	@RequestMapping(path="/reset-password", method=RequestMethod.POST)
	public String resetPassword(@RequestParam("username") String username, Model model){
		logger.debug("resetPassword() method was invoked for username = {}", username);
		
		try{
			userService.createPasswordToken(username);
			model.addAttribute("title", "Change password");
			model.addAttribute("topic", "Check your mailbox");
			return "info";		
		} catch (UserNotFoundException e) {
			logger.warn("User not exist: ", e);			
			return "error";
		}		
	}
	
	/**
	 * Add password token to Model and returns changePassword as name of .jsp file will be displayed
	 * @param passwordToken - token which created especially for changing password
	 * @param model - {@code Model} instance will be filled
	 * @return "changePassword" always
	 */
	@RequestMapping(path="/change-password", method=RequestMethod.GET)
	public String changePassword(@RequestParam("token") String passwordToken, Model model){
		logger.debug("changePassword() GET method was invoked for passwordToken = {}", 
						passwordToken);
		
		model.addAttribute("passwordToken", passwordToken);
		
		//TODO change .css for this view
		return "changePassword";
	}
	
	/**
	 * Chages password for user with such {@code passwordToken}, 
	 * returns name of .jsp file will be displayed
	 * @param passwordToken - token created especially for changing password
	 * @param password - desirable password
	 * @param passwordConfirmation - confirmation of desirable password
	 * @param model - {@code Model} instance will be filled
	 * @return "error" - if such token doesn't exist or password and confirmation are not equal,
	 * "info" - if all went right
	 */
	@RequestMapping(path="/change-password", method=RequestMethod.POST)
	public String changePassword(@RequestParam("passwordToken") String passwordToken,
								 @RequestParam("password") String password, 
								 @RequestParam("passwordConfirmation") String passwordConfirmation,
								 Model model){	
		logger.debug("changePassword() POST method was invoked for passwordToken = {}", 
						passwordToken);
		
		if(!password.equals(passwordConfirmation)) return "error";
		if(!password.matches(PASSWORD_PATTERN)) return "error";
		try {
			userService.changePassword(passwordToken, passwordConfirmation);
			model.addAttribute("title", "Change password");
			model.addAttribute("topic", "Password was changed successfully");
			return "info";
		} catch (PasswordTokenNotFoundException e) {
			logger.warn("Exception happened during changing password, ", e);
			return "error";
		}		
	}
	
}
