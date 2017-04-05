package com.vetalzloy.projectica.service.dao;

import com.vetalzloy.projectica.model.ChatRoom;

/**
 * This interface provides methods for working with chat rooms in database level
 * @author VetalZloy
 *
 */
public interface ChatRoomDAO {
	
	/**
	 * Retrieves chat room by {@code id}
	 * @param id - id of necessary chatroom
	 * @return retrieved chatroom or null, if it doesn't exist
	 */
	ChatRoom getById(int id);
	
	/**
	 * Saves new chatroom or updates, if it already exists.
	 * @param room - instance of chatroom should be updated or saved
	 */
	void saveOrUpdate(ChatRoom room);
	
}
