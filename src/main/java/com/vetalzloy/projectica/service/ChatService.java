package com.vetalzloy.projectica.service;

import com.vetalzloy.projectica.model.ChatRoom;
import com.vetalzloy.projectica.model.User;
import com.vetalzloy.projectica.service.exception.AccessDeniedException;
import com.vetalzloy.projectica.service.exception.ChatRoomAlreadyExistsException;
import com.vetalzloy.projectica.service.exception.ChatRoomNotFoundException;
import com.vetalzloy.projectica.service.exception.ExternalResourceAccessException;
import com.vetalzloy.projectica.service.exception.ProjectNotFoundException;

/**
 * This interface provides methods for working with chat rooms and chat messages.
 * @author VetalZloy
 *
 */
public interface ChatService {
	
	/**
	 * Either retrieves <b>{@code ChatRoom}</b> instance, which has <b>{@code id}</b> or throws one of the exceptions.
	 * @param id - id of required chatroom
	 * @return <b>{@code ChatRoom}</b> instance with such <b>{@code id}</b>
	 * @throws AccessDeniedException if current user doesn't belong to project, 
	 * which contain chatroom with such <b>{@code id}</b>
	 * @throws ChatRoomNotFoundException if chatroom with such <b>{@code id}</b> doesn't exist
	 */
	ChatRoom getById(int id) throws AccessDeniedException, ChatRoomNotFoundException;
	
	/**
	 * Creates chatroom. If it was created successfully, returns it. 
	 * Else one of exceptions will be thrown.
	 * @param projectId - id of project for which chatroom should be created.
	 * @param name - name of required chatroom
	 * @return created chatroom (never {@code null})
	 * @throws AccessDeniedException if current user is not creator of project with {@code projectId}
	 * @throws ProjectNotFoundException if project with such {@code projectId} doesn't exist
	 * @throws ChatRoomAlreadyExistsException if chatroom with such {@code name} 
	 * exists in project with such {@code projectId}
	 * @throws ExternalResourceAccessException - if error occurs in messaging system
	 */
	ChatRoom createChatRoom(int projectId, String name) throws AccessDeniedException, ProjectNotFoundException, ChatRoomAlreadyExistsException, ExternalResourceAccessException;
	
	/**
	 * Adds users to chatroom
	 * @param room - chatroom which will contain new users
	 * @param users - array of users will be added to chatroom
	 * @throws ExternalResourceAccessException - if some error happened in messaging system
	 */
	void addUsersToChatRoom(ChatRoom room, User... users ) throws ExternalResourceAccessException;
	
	/**
	 * Removes users from chatroom
	 * @param room - chatroom from which users will be removed
	 * @param users - array of users will be removed from chatroom
	 * @throws ExternalResourceAccessException - if some error happened in messaging system
	 */
	void removeUsersFromChatRoom(ChatRoom room, User... users ) throws ExternalResourceAccessException;
	
}
