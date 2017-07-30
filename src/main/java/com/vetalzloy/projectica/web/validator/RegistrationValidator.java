package com.vetalzloy.projectica.web.validator;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.vetalzloy.projectica.service.UserService;
import com.vetalzloy.projectica.service.exception.UserNotFoundException;
import com.vetalzloy.projectica.web.form.RegistrationForm;

/**
 * Validator of {@link RegistrationForm}
 * @author VetalZloy
 *
 */
@Component
public class RegistrationValidator implements Validator{

	private static final Logger logger = LoggerFactory.getLogger(RegistrationValidator.class);
	
	@Autowired
	private UserService userService;
	
	private static final List<String> forbiddenUsernames = Arrays.asList("anonymousUser", 
																		 "admin", 
																		 "VetalZloy");
	
	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*"
			+ "@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	
	private static final String USERNAME_PATTERN = "([A-Za-z]|\\d){5,}";
	
	private static final String PASSWORD_PATTERN = "([A-Za-z]|\\d){5,}";
	
	@Override
	public boolean supports(Class<?> clazz) {
		return RegistrationForm.class.equals(clazz);
	}

	@Override
	public void validate(Object o, Errors errors) {
		logger.debug("Validating registration information ...");
		
		RegistrationForm form = (RegistrationForm) o;
		
		String username = form.getUsername();
		
		if(!username.matches(USERNAME_PATTERN)) {
			errors.rejectValue("username", "valid.username");
			logger.debug("Bad username format: '{}'", username);
		}
			
		if(!form.getPassword().matches(PASSWORD_PATTERN)) {
			errors.rejectValue("password", "valid.password");
			logger.debug("Bad password format: '{}'", form.getPassword());
		}			
		
		if(!form.getPasswordConf().equals(form.getPassword())) {
			errors.rejectValue("passwordConf", "valid.passwordConf");
			logger.debug("Password and password confirmation are different, for username '{}'",
					username);
		}
			
		
		if(!form.getEmail().matches(EMAIL_PATTERN)) {
			errors.rejectValue("email", "valid.email");
			logger.debug("Bad email format: '{}'", form.getEmail());
		}			
		
		try{
			userService.getByEmail(form.getEmail());
			
			//if user exists
			errors.rejectValue("email", "valid.emailExists");
			logger.debug("Email '{}' already exists.", form.getEmail());			
		} catch(UserNotFoundException e){
			//if user doesn't exist
			logger.debug("User with email '{}' doesn't exist.", form.getEmail());
		}
		
		try {
			userService.getByUsername(username);
			
			//if user exists
			errors.rejectValue("username", "valid.userExist");
			logger.debug("User with username '{}' already exists.", username);
		} catch (UserNotFoundException e) {
			//if user doesn't exist
			logger.debug("User with username '{}' doesn't exist.", username);
		}
		
		boolean forbiddenUsername = forbiddenUsernames
									.stream()
									.map(f -> f.toLowerCase())
									.anyMatch(fl -> fl.equals(username.toLowerCase()));
		if(forbiddenUsername) {
			errors.rejectValue("username", "valid.username");
			logger.debug("Forbidden username '{}'", username);
		}			
		
		if(errors.getErrorCount() == 0)
			logger.debug("Validating registration information for username {} and email {} completed succsesfully",
				username, form.getEmail());
		else logger.debug("{} errors happened during validating registration information for username {} and email {}.",
				errors.getErrorCount(), username, form.getEmail());
	}

}
