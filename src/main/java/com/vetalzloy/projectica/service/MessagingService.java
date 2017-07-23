package com.vetalzloy.projectica.service;

import com.vetalzloy.projectica.model.ChatRoom;
import com.vetalzloy.projectica.model.User;
import com.vetalzloy.projectica.service.exception.ExternalResourceAccessException;

/**
 * This interface provides methods for working with extermal messaging system.
 * @author VetalZloy
 *
 */
public interface MessagingService {
	
	/**
	 * Adds users to chatroom
	 * @param room - chatroom which will contain new users
	 * @param users - array of users will be added to chatroom
	 * @throws ExternalResourceAccessException - if credentials are invalid or if jwt is expired
	 */
	void addUsersToChatRoom(ChatRoom room, User... users) throws ExternalResourceAccessException;
	
	/**
	 * Removes users from chatroom
	 * @param room - chatroom from which users will be removed
	 * @param users - array of users will be removed from chatroom
	 * @throws ExternalResourceAccessException - if credentials are invalid or if jwt is expired
	 */
	void removeUsersFromChatRoom(ChatRoom room, User... users) throws ExternalResourceAccessException;
	
}
