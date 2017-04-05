package com.vetalzloy.projectica.service.dao;

import java.util.List;

import com.vetalzloy.projectica.model.DialogMessage;

/**
 * This interface provides methods for working with dialog messages in database level
 * @author VetalZloy
 *
 */
public interface DialogMessageDAO {
	
	/**
	 * Saves new message or update, if it already exists
	 * @param message
	 */
	void saveOrUpdate(DialogMessage message);
	
	/**
	 * Retrieves last messages in dialog between users with id: {@code userId1} and {@code userId2}
	 * @param amount - max amount of messages to retrieve
	 * @param userId1 - id of first user (order doesn't important)
	 * @param userId2 - id of second user (order doesn't important)
	 * @return List of retrieved messagesordered by sending date in descending order,
	 * can be empty, nut never {@code null}
	 */
	List<DialogMessage> getFirstPage(int amount, long userId1, long userId2);
	
	/**
	 * Retrieves messages with ids lower than {@code earliestId}
	 * @param earliestMessageId - id of message which has bigger than required messages
	 * and which is closest to them
	 * @param amount - max amount of messages will be retrieved
	 * @param userId1 - id of first user (order doesn't important)
	 * @param userId2 - id of second user (order doesn't important)
	 * @return List of retrieved messages ordered by sending date in descending order,
	 * can be empty, nut never {@code null}
	 */
	List<DialogMessage> getPageBeforeEarliest(long earliestId, int amount, long userId1, long userId2);
	
}
