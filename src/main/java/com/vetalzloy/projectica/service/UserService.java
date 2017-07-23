package com.vetalzloy.projectica.service;

import java.util.List;

import com.vetalzloy.projectica.model.PasswordToken;
import com.vetalzloy.projectica.model.User;
import com.vetalzloy.projectica.service.exception.EntityNotFoundException;
import com.vetalzloy.projectica.service.exception.PasswordTokenNotFoundException;
import com.vetalzloy.projectica.service.exception.UserAlreadyExistsException;
import com.vetalzloy.projectica.service.exception.UserNotFoundException;
import com.vetalzloy.projectica.service.exception.VerificationTokenNotFoundException;

/**
 * This interface provides methods for working with users
 * @author VetalZloy
 *
 */
public interface UserService {
	
	/**
	 * Returns list of appropriate users.
	 * @param usernamePattern - users must have names which match to this pattern.
	 * Not RegExp just username or part of username ("Will", "An")
	 * @param tags - tags, all of which requested users must have
	 * @return list of appropriate users, can be empty, but never {@code null}. 
	 * If usernamePattern and tags are empty or nulls, empty list will be returned
	 */
	List<User> getSimilars(String usernamePattern, List<String> tags);
	
	/**
	 * Retrieves page with users. Amount of users is under control of implementation
	 * @param page - number of requested page
	 * @return list of users, can be empty, but never {@code null}
	 */
	List<User> getUsersPage(int page);
	
	/**
	 * Retrieves user with such {@code username} and, if it exists, 
	 * loads <b>some</b> "LAZY" collections and returns user. Else throws exception.
	 * Set of loadeble "LAZY" collections is under control implementation
	 * @param username - username of requested user
	 * @return retrieved user with some loaded "LAZY" collections
	 * @throws UserNotFoundException if user with such {@code username} doesn't exist
	 */
	User getFullByUsername(String username) throws UserNotFoundException;
	
	/**
	 * Retrieves user with such {@code username} and, if it exists returns it. 
	 * Else throws exception. "LAZY" collections won't be loaded.
	 * @param username - username of requested user
	 * @return retrieved user without "LAZY" collections
	 * @throws UserNotFoundException if user with such {@code username} doesn't exist
	 * @see getFullByUsername(String username)
	 */
	User getByUsername(String username) throws UserNotFoundException;
	
	/**
	 * Retrieves user with such {@code username} and, if it exists returns it. 
	 * Else throws exception. "LAZY" collections won't be loaded.
	 * @param email - email of requested user
	 * @return retrieved user without "LAZY" collections
	 * @throws UserNotFoundException if user with such {@code username} doesn't exist
	 * @see getFullByUsername(String username)
	 */
	User getByEmail(String email)  throws UserNotFoundException;
	
	/**
	 * Updates information about current user.
	 * @param name - name of current username, which will be updated
	 * @param surname - surname of current username, which will be updated
	 * @param cvLink - cvLink of current username, which will be updated
	 * @throws UserNotFoundException if current user is not registered
	 */
	void update(String name, String surname, String cvLink) throws UserNotFoundException;
	
	/**
	 * Creates password token to reset password of user with such {@code username} 
	 * and, if token is created successfully, sends message to user's email and returns token.
	 * Else throws exception.
	 * @param username - username of user, who request password resetting
	 * @return created password token
	 * @throws UserNotFoundException if user with such {@code username} doesn't exist
	 */
	PasswordToken createPasswordToken(String username) throws UserNotFoundException;	
	
	/**
	 * Retrieves user by {@code passwordToken} and changes his password
	 * @param passwordToken - password token which point to user, who want to reset password
	 * @param password - new password
	 * @throws PasswordTokenNotFoundException - if such {@code passwordToken} doesn't exist
	 */
	void changePassword(String passwordToken, String password) throws PasswordTokenNotFoundException;
	
	/**
	 * Creates and saves new user, but it still unenabled, 
	 * sends message with registration confirm link to {@code email}
	 * @param username - desirable username
	 * @param email - desirable email
	 * @param password - desirable password
	 * @throws UserAlreadyExistsException if user with such {@code username} 
	 * or {@code email} already exists.
	 * But actually {@code RegistrationValidator} filtered such users before.
	 * @see com.vetalzloy.projectica.web.validator.RegistrationValidator
	 */
	void register(String username, String email, String password) throws UserAlreadyExistsException;
	
	/**
	 * Retrieves user by {@code token} and makes him enabled.
	 * @param token - verification token, created before
	 * @throws UserAlreadyExistsException if user with such verification token is already enabled
	 * @throws VerificationTokenNotFoundException if such {@code token} doesn't exist
	 * @throws UserNotFoundException for detail look at 'see' section
	 * @see com.vetalzloy.projectica.service.DialogService#sendAfterRegistrationMessage()
	 */
	void activateByVerificationToken(String token) throws EntityNotFoundException, UserAlreadyExistsException;
	
	/**
	 * Checks existence of user
	 * @param valueToCheck - username or email for checking
	 * @return {@code true} if user exists, else {@code false}
	 */
	boolean isExists(String valueToCheck);

	/**
	 * Retrieves user by {@code userId}
	 * @param userId - id of necessary user
	 * @return user, who has such a {@code userId}
	 * @throws UserNotFoundException - if user with such {@code userId} doesn't exist
	 */
	User getById(long userId) throws UserNotFoundException;
	
}
