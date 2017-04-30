package com.vetalzloy.projectica.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.vetalzloy.projectica.model.Interlocutor;
import com.vetalzloy.projectica.model.PasswordToken;
import com.vetalzloy.projectica.model.User;
import com.vetalzloy.projectica.model.VerificationToken;
import com.vetalzloy.projectica.service.dao.UserDAO;
import com.vetalzloy.projectica.service.exception.PasswordTokenNotFoundException;
import com.vetalzloy.projectica.service.exception.UserAlreadyExistsException;
import com.vetalzloy.projectica.service.exception.UserNotFoundException;
import com.vetalzloy.projectica.service.exception.VerificationTokenNotFoundException;
import com.vetalzloy.projectica.util.MailUtil;
import com.vetalzloy.projectica.util.SecurityUtil;
import com.vetalzloy.projectica.web.json.UserJson;

@Service
@Transactional
public class UserServiceImpl implements UserService{
	
	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Autowired
	private DialogService dialogService;
	
	@Autowired
	@Qualifier("applicationAddress")
	private String applicationAddress;
	
	@Autowired
	private BCryptPasswordEncoder encoder;

	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private MailUtil mailUtil;

	@Override
	public Set<Interlocutor> getInterlocutors() throws UserNotFoundException {
		String currentUsername = SecurityUtil.getCurrentUsername();
		
		logger.debug("Extracting interlocutors for user with username '{}'...", 
						currentUsername);
		
		User user = userDAO.getByUsername(currentUsername);
		if(user == null)
			throw new UserNotFoundException("User with username '" + currentUsername +"' doesn't exist");
		
		loadInterlocutors(user);		
		logger.info("Interlocutors for user with username '{}' were extracted successfully; interlocutors amount = {}",
						currentUsername, user.getInterlocutors().size());
		
		return user.getInterlocutors();
	}
	
	@Override
	public List<User> getSimilars(String usernamePattern, List<String> tags) {
		if((usernamePattern == null || "".equals(usernamePattern)) && 
		   (tags == null || tags.size() == 0)) {
			return new ArrayList<>();
		}			
			
		if(tags == null) tags = new ArrayList<>();
		if(usernamePattern == null) usernamePattern = "";
		
		logger.debug("Retreiving users with username like '{}' with definite tags.", usernamePattern);	
		
		return userDAO.getSimilars(usernamePattern, tags);
	}
	
	@Override
	public User getByUsername(String username) throws UserNotFoundException {
		logger.debug("Retrieving user by username {} ...", username);
		User user = userDAO.getByUsername(username);
		if(user == null ) 
			throw new UserNotFoundException("User with username '"
											+ username + "' is not found.");
		return user;		
	}

	@Override
	public User getByEmail(String email) throws UserNotFoundException {
		logger.debug("Retrieving user by email {} ...", email);
		User user = userDAO.getByEmail(email);
		if(user == null) throw new UserNotFoundException("User with email " + email + " is not found.");
		
		return userDAO.getByEmail(email);
	}

	@Override
	public void update(String name, String surname, String cvLink) throws UserNotFoundException {
		String currentUsername = SecurityUtil.getCurrentUsername();
		logger.debug("Updating user information {} ...", currentUsername);
		User user = userDAO.getByUsername(currentUsername);
		if(user == null) 
			throw new UserNotFoundException("User with username '" 
											+ currentUsername 
											+ "' doesn't exist");
		
		if("".equals(name))
			name = null;
		
		if("".equals(surname))
			surname = null;
		
		if("".equals(cvLink))
			cvLink = null;
		
		user.setName(name);
		user.setSurname(surname);
		user.setCvLink(cvLink);
		
		userDAO.saveOrUpdate(user);
	}

	@Override
	public PasswordToken createPasswordToken(String username) throws UserNotFoundException {
		logger.debug("Creating passwordToken for username {} ..." , username);
		
		User user = userDAO.getByUsername(username);
		if(user == null) throw new UserNotFoundException("User " + username +  " not exist");
		
		String passwordTokenString = UUID.randomUUID().toString();
		PasswordToken token = new PasswordToken(user, passwordTokenString);
		token.setExpireDate(LocalDateTime.now().plusHours(24));
		user.setPasswordToken(token);
		userDAO.saveOrUpdate(user);
		
		String text = "For changing password go to: \n" + 
				applicationAddress + "/change-password?token=" + passwordTokenString;
		
		mailUtil.sendMail(user.getEmail(), "Reset password", text);
		
		return token;
	}

	@Override
	public void changePassword(String passwordToken, String password) throws PasswordTokenNotFoundException {
		logger.debug("Changing password by passwordtoken = {} ...", passwordToken);
		
		User user = userDAO.getByPasswordToken(passwordToken);		
		if(user == null) 
			throw new PasswordTokenNotFoundException("Password token " + passwordToken + " not exist");
		
		user.setPassword(encoder.encode(password));		
		userDAO.saveOrUpdate(user);
		
		userDAO.deletePasswordToken(user);
	}

	@Override
	public void register(String username, String email, String password) throws UserAlreadyExistsException {
		logger.debug("Creating verififcation token and saving user with username '{}' and email '{}' ...", 
						username, email);
		
		if(userDAO.getByUsername(username) != null)
			throw new UserAlreadyExistsException("User with username '" + username + "' already exists");
		
		if(userDAO.getByEmail(email) != null)
			throw new UserAlreadyExistsException("User with email '" + email + "' already exists");
		
		String encodedPassword = encoder.encode(password);
		User user = new User(username, email, encodedPassword);
		
		String tokenString = UUID.randomUUID().toString();		
		LocalDateTime ldt = LocalDateTime.now().plusHours(24);
		
		VerificationToken vt = new VerificationToken(tokenString, ldt, user);
		user.setVerificationToken(vt);
		
		userDAO.saveOrUpdate(user);
		
		try{
			String text = "TY and go to: \n" + 
							applicationAddress + "/registrationConfirm?token=" + vt.getToken();
			
			mailUtil.sendMail(user.getEmail(), "Registration", text);
		} catch(Exception e){
			logger.warn("Error happened during sending email", e);
			throw e;
		}		
				
	}

	@Override
	public synchronized void activateByVerificationToken(String token) 
									throws VerificationTokenNotFoundException, 
										   UserNotFoundException, 
										   UserAlreadyExistsException {
		logger.debug("Activating user by verificationToken = {}", token);
		
		User user = userDAO.getByVerificationToken(token);
		
		if(user == null)
			throw new VerificationTokenNotFoundException("Verification token " + token + " not exist");
		
		if(user.isEnabled()) 
			throw new UserAlreadyExistsException("User with verification token '" + token + "' is already activated");
		
		user.setEnabled(true);
		userDAO.saveOrUpdate(user);
		
		//Send message from admin
		dialogService.sendAfterRegistrationMessage(user.getUsername());
		
	}

	@Override
	public List<User> getUsersPage(int start) {
		int amount = 25;
		logger.debug("Retreiving page with {} users from {} ...", amount, amount*start);
		return userDAO.getUsersPage(start*amount, amount);
	}

	@Override
	public User getFullByUsername(String username) throws UserNotFoundException {
		logger.debug("Retrieving  full information for user with username {}", username);
		User user = userDAO.getFullByUsername(username);
		if(user == null) 
			throw new UserNotFoundException("User with username " + username + " doesn't exist");
				
		return user;
	}

	@Override
	public UserJson getCurrentUserJson() throws UserNotFoundException {
		String username = SecurityUtil.getCurrentUsername();
		return getUserJsonByUsername(username);		
	}

	@Override
	public UserJson getUserJsonByUsername(String username) throws UserNotFoundException {
		int gravatarSize = 40;
		
		logger.debug("Retreiving user with username '{}'", username);
		User user = userDAO.getByUsername(username);
		
		if(user == null)
			throw new UserNotFoundException("User with username '" + username +  "' doesn't exist");
		
		return UserJson.create(user, gravatarSize);
	}

	@Override
	public boolean isExists(String valueToCheck) {
		logger.debug("Checking value '{}' for existence.", valueToCheck);
		
		try {
			if(valueToCheck.contains("@")) // value is email
				getByEmail(valueToCheck);
			else getByUsername(valueToCheck); //value is username
			
			//if exception wasn't thrown, then such value exists
			logger.debug("User with field value '{}' exists.", valueToCheck);
			return true;
		} catch(UserNotFoundException e) {
			logger.debug("User with field value '{}' doesn't exist.", valueToCheck);
			return false;
		}
	}

	@Override
	public void loadInterlocutors(User user) {
		userDAO.loadInterlocutors(user);
	}
	
}
