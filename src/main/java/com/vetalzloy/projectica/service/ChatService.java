package com.vetalzloy.projectica.service;

import java.util.List;

import com.vetalzloy.projectica.model.ChatMessage;
import com.vetalzloy.projectica.model.ChatRoom;
import com.vetalzloy.projectica.service.exception.AccessDeniedException;
import com.vetalzloy.projectica.service.exception.ChatRoomAlreadyExistsException;
import com.vetalzloy.projectica.service.exception.ChatRoomNotFoundException;
import com.vetalzloy.projectica.service.exception.EntityNotFoundException;
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
	 */
	ChatRoom createChatRoom(int projectId, String name) throws AccessDeniedException, ProjectNotFoundException, ChatRoomAlreadyExistsException;
	
	/**
	 * Create chat message. If it was created successfully, returns it. Else throws an exception.
	 * @param chatRoomId - id of chatRoom for which message should be created
	 * @param text - text of message
	 * @return instance of created {@code ChatMessage} (never {@code null})
	 * @throws AccessDeniedException if current user doesn't belong to project, 
	 * which contain chatroom with such <b>{@code chatRoomId}</b>
	 * @throws EntityNotFoundException if current user is not registered 
	 * or if chatroom with such {@code chatRoomId} doesn't exist
	 */
	ChatMessage createMessage(int chatRoomId, String text) throws AccessDeniedException, EntityNotFoundException;
	
	/**
	 * Returns the first page of chat messages from chatroom with such {@code chatRoomId}. 
	 * Amount of messages is managed by implementation.
	 * @param chatRoomId - id of chatroom for which messages should be retrieved
	 * @return List of retrieved messages ordered by sending date in descending order,
	 * can be empty, but never {@code null}
	 * @throws AccessDeniedException if current user doesn't belong to project, 
	 * which contain chatroom with such <b>{@code chatRoomId}</b>
	 * @throws ChatRoomNotFoundException if chatroom with such {@code chatRoomId} doesn't exist
	 */
	List<ChatMessage> getFirstPage(int chatRoomId)  throws AccessDeniedException, ChatRoomNotFoundException;
	
	/**
	 * Returns page of chat messages which have ids lower than {@code earliestId} 
	 * from chatroom with such {@code chatRoomId}. 
	 * Amount of messages is managed by implementation.
	 * @param earliestId - id which must be higher than all ids in retrieved messages 
	 * and must be the closest to them.
	 * @param chatRoomId - id of chatroom for which messages should be retrieved 
	 * @return List of retrieved messages ordered by sending date in descending order,
	 * can be empty, but never {@code null}
	 * @throws AccessDeniedException if current user doesn't belong to project, 
	 * which contain chatroom with such <b>{@code chatRoomId}</b>
	 * @throws ChatRoomNotFoundException if chatroom with such {@code chatRoomId} doesn't exist
	 */
	List<ChatMessage> getPageBeforeEarliest(long earliestId, int chatRoomId)  throws AccessDeniedException, ChatRoomNotFoundException;
	
}
