package com.vetalzloy.projectica.service.dao;

import java.util.List;

import com.vetalzloy.projectica.model.User;

/**
 * This interface provides methods fot working with tags on database level
 * @author VetalZloy
 *
 */
public interface UserDAO {
	
	/**
	 * Retrieves users with username matched to {@code username} 
	 * and which have <b>ALL</b> of {@code tags}
	 * @param usernamePattern - all user will be retrieved should have username, which matches to it 
	 * (not RegExp, just username or part of it: "VetalZloy", "An", etc)
	 * @param tags - tags which should belong to users will be retrieved
	 * @return List of retrieved users, can be empty, but never {@code null}
	 */
	List<User> getSimilars(String usernamePattern, List<String> tags);
	
	/**
	 * Retrieves users which have id lower than {@code start}
	 * @param start - all users which will be retrieved should have id lower than {@code start},
	 * and {@code start} should be closest number to them
	 * @param amount - max amount of positions wil be retreived
	 * @return List of retrieved users, can be empty, but never {@code null}
	 */
	List<User> getUsersPage(int start, int amount);
	
	/**
	 * Retrieves user with such {@code username}.
	 * LAZY fields won't be loaded.
	 * @param username - username of necessary user
	 * @return retrieved user or {@code null}, if he doesn't exist
	 */
	User getByUsername(String username);
	
	/**
	 * Retrieves user with such {@code username}.
	 * LAZY fields will be loaded, except interlocutors
	 * @param username - username of necessary user
	 * @return retrieved user or {@code null}, if he doesn't exist
	 */
	User getFullByUsername(String username);
	
	/**
	 * Saves new user or updates, if he already exists
	 * @param user - user which will be saved or updated
	 */
	void saveOrUpdate(User user);
	
	/**
	 * Retrieves user with such {@code email}
	 * @param email - email of necessary user
	 * @return retrieved user or {@code null}, if he doesn't exist
	 */
	User getByEmail(String email);

	/**
	 * Retrieves user with such {@code userId}
	 * @param userId - id of necessary user
	 * @return retrieved user or {@code null}, if he doesn't exist
	 */
	User getById(long userId);
	
	/**
	 * Retrieves user with such {@code verificationToken}
	 * @param verificationToken - token of necessary user
	 * @return retrieved user or {@code null}, if he doesn't exist
	 */
	User getByVerificationToken(String verificationToken);
	
	/**
	 * Retrieves user with such {@code passwordToken}
	 * @param passwordToken - token of necessary user
	 * @return retrieved user or {@code null}, if he doesn't exist
	 */
	User getByPasswordToken(String passwordToken);
	
	/**
	 * Deletes users, who have expired verification token and who are enabled
	 */
	void deleteUsersWithExpiredVerificationToken();
	
	/**
	 * Deletes password token which belongs to {@code user}
	 * @param user - user who contain password token will be deleted
	 */
	void deletePasswordToken(User user);
	
	/**
	 * Deletes expired password tokens
	 */
	void deleteExpiredPasswordTokens();
	
}
