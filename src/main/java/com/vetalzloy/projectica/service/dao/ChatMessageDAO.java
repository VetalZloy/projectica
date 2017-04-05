package com.vetalzloy.projectica.service.dao;

import java.util.List;

import com.vetalzloy.projectica.model.ChatMessage;

/**
 * This interface provides methods for working with chat messages on database level
 * @author VetalZloy
 *
 */
public interface ChatMessageDAO {
	
	/**
	 * Saves new chat message or updates, if message already exists
	 * @param message
	 */
	void saveOrUpdate(ChatMessage message);
	
	/**
	 * Retrieves last messages for chatroom with id {@code chtroomId} in amount of {@code amount}
	 * @param amount - max amount of messages will be retrieved
	 * @param chatRoomId - id of chatroom from which messages will be retrieved
	 * @return List of retrieved messages ordered by sending date in descending order,
	 * can be empty, but never {@code null}
	 */
	List<ChatMessage> getFirstPage(int amount, int chatRoomId);
	
	/**
	 * Retrieves messages with ids lower than {@code earliestId} 
	 * from chatroom with id {@code chatRoomId}
	 * @param earliestId - number that should be bigger than id of messages will be retrieved
	 * and which is closest to them
	 * @param amount - max amount of messages will be retrieved
	 * @param chatRoomId - id of chatroom from which messages will be retrieved
	 * @return List of retrieved messages ordered by sending date in descending order,
	 * can be empty, but never {@code null}
	 */
	List<ChatMessage> getPageBeforeEarliest(long earliestId, int amount, int chatRoomId);
	
	
}
