package com.vetalzloy.projectica.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.vetalzloy.projectica.service.UserService;
import com.vetalzloy.projectica.service.exception.EntityNotFoundException;
import com.vetalzloy.projectica.service.exception.UserAlreadyExistsException;
import com.vetalzloy.projectica.util.SecurityUtil;
import com.vetalzloy.projectica.web.form.RegistrationForm;
import com.vetalzloy.projectica.web.validator.RegistrationValidator;

/**
 * Controller class which deal with "/" requests (index page and registration)
 * @author VetalZloy
 *
 */
@Controller
@RequestMapping("/")
public class IndexController {
	
	private static final Logger logger = LoggerFactory.getLogger(IndexController.class);
	
	@Autowired
	private RegistrationValidator registrationValidator;
	
	@Autowired
	private UserService userService;
	
	/**
	 * Handles favicon requests
	 * @return favicon.ico url
	 */
	@RequestMapping("favicon.ico")
	public String favicon(){		
		return "forward:/img/favicon.ico";
	}
	
	/**
	 * Handles request to get home page and returns name of .jsp file will be displayed
	 * @param model - {@code Model} instance will be filled
	 * @return "index" - if current user is not logged in,
	 * else redirects to personal page
	 */
	@RequestMapping("/")
	public String index(Model model){
		String username = SecurityUtil.getCurrentUsername();
		logger.debug("index() method was invoked for username {}", username);
		if(username != null && !"".equals(username) && !"anonymousUser".equals(username)) 
			return "redirect:/users/" + username;
		
		model.addAttribute("registrationForm", new RegistrationForm());
		return "index";
	}
	
	/**
	 * Handles request to register new user and returns name of .jsp file 
	 * will be displayed 
	 * @param form - instance of {@code RegistrationForm} class which has all typed by user data
	 * @param result - object which will has data about {@code form} validity
	 * @param model - {@code Model} instance will be filled
	 * @return - "index" - if data typed by user is not valid,<br>
	 * "request" - if data valid and message was sent,<br>
	 * "error" - if something went wrong
	 */
	@RequestMapping(method=RequestMethod.POST)
	public String registration(@ModelAttribute RegistrationForm form, 
											   BindingResult result, 
											   Model model){
		logger.debug("registration() method was invoked for username = {} and email = {}", 
				form.getUsername(), form.getEmail());
		
		registrationValidator.validate(form, result);
		
		if(result.hasErrors()){
			model.addAttribute("registrationForm", form);
			return "index";
		}		
		
		try{
			userService.register(form.getUsername(), 
								 form.getEmail(), 
								 form.getPassword());
			model.addAttribute("title", "Registration");
			model.addAttribute("topic", "Check your mailbox");
			return "info";
		} catch(Exception e){
			//May be error happened during mail sending
			logger.warn("Some error happened during registration process", e);
			return "error";
		}
	}
	
	/**
	 * Handles request to confirm registration and 
	 * returns name of .jsp file will be diplayed
	 * @param token - verification token which shows current user
	 * @param model - {@code Model} instance will be filled
	 * @return "info" - if user was activated successfully,<br>
	 * "error" - if something went wrong
	 */
	@RequestMapping(path="/registrationConfirm")
	public String registrationConfirm(@RequestParam("token") String token, Model model){
		logger.debug("registrationConfirm() method was invoked for token = {}", token);
		
		try {
			userService.activateByVerificationToken(token);			
			model.addAttribute("title", "Registration");
			model.addAttribute("topic", "Registration complited");
			model.addAttribute("additions", "Please, <a href='https://projectica.me'>Log in</a>");
			return "info";
		} catch (EntityNotFoundException | UserAlreadyExistsException e) {
			logger.warn("Error happened during confirming registration", e);
			return "error";
		}
	}
	
	@RequestMapping("/about")
	public String about(){
		return "about";
	}
	
	/**
	 * This method is never invoked, IDK why i need it
	 * @param req
	 * @param res
	 * @return
	 */
	@RequestMapping(path="/logout")
	public String logout(HttpServletRequest req, HttpServletResponse res){
		String name = SecurityUtil.getCurrentUsername();
		logger.info("Logout for user {}", name);
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth != null) 
			new SecurityContextLogoutHandler().logout(req, res, auth);
		
		return "index";
	}
	
}
