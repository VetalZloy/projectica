package com.vetalzloy.projectica.service;

import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vetalzloy.projectica.model.ChatMessage;
import com.vetalzloy.projectica.model.ChatRoom;
import com.vetalzloy.projectica.model.Project;
import com.vetalzloy.projectica.model.User;
import com.vetalzloy.projectica.service.dao.ChatMessageDAO;
import com.vetalzloy.projectica.service.dao.ChatRoomDAO;
import com.vetalzloy.projectica.service.exception.AccessDeniedException;
import com.vetalzloy.projectica.service.exception.ChatRoomAlreadyExistsException;
import com.vetalzloy.projectica.service.exception.ChatRoomNotFoundException;
import com.vetalzloy.projectica.service.exception.EntityNotFoundException;
import com.vetalzloy.projectica.service.exception.ProjectNotFoundException;
import com.vetalzloy.projectica.util.SecurityUtil;

@Service
@Transactional
public class ChatServiceImpl implements ChatService {

	private static final Logger logger = LoggerFactory.getLogger(ChatServiceImpl.class);
	
	@Autowired
	private ChatRoomDAO chatRoomDAO;
	
	@Autowired
	private ChatMessageDAO chatMessageDAO;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ProjectService projectService;
	
	@Override
	public ChatRoom getById(int id) throws AccessDeniedException, ChatRoomNotFoundException {
		String currentUsername = SecurityUtil.getCurrentUsername();
		logger.debug("Retrieving chatroom with id = {} ...", id);
		
		ChatRoom room = chatRoomDAO.getById(id);
		if(room == null) throw new ChatRoomNotFoundException(id);
		
		logger.debug("Checking existence of user with username '{}' in project with chatroom with id = {}",
				currentUsername, id);
		
		//checking whether user belongs to project
		boolean belongingToProject = room.getProject()
										 .getPositions()
										 .stream()
										 .filter(pos -> pos.getUser() != null)
										 .filter(pos -> pos.getFiringDate() == null)
										 .map(pos -> pos.getUser().getUsername())
										 .anyMatch(u -> u.equals(currentUsername));
		
		if(! belongingToProject) 
			throw new AccessDeniedException("Trying get access to chat room with id = " + id
										+ " by user with username " + currentUsername);
				
		return room;
	}

	@Override
	public ChatRoom createChatRoom(int projectId, String name) throws AccessDeniedException, ProjectNotFoundException, ChatRoomAlreadyExistsException {
		String currentUsername = SecurityUtil.getCurrentUsername();
		Project project = projectService.getById(projectId);
		
		//checking whether chat room with such name exists
		boolean existance = project.getChatRooms()
								   .stream()
								   .anyMatch(r -> r.getName().equals(name));
		if(existance) throw new ChatRoomAlreadyExistsException("Chatroom with name = " + name + 
															  " in project with id = " + projectId + 
															  " already exists.");
		
		//checking whether current user is creator of appropriate project
		if(! project.getCreator().getUsername().equals(currentUsername))
			throw new AccessDeniedException("Trying to create chatroom for project with id = "
											+ projectId + " by user with username = " + currentUsername);
		
		ChatRoom room = new ChatRoom(name, project);
		chatRoomDAO.saveOrUpdate(room);
		
		return room;
	}
	
	@Override
	public ChatMessage createMessage(int chatRoomId, String text) throws AccessDeniedException, EntityNotFoundException {
		String currentUsername = SecurityUtil.getCurrentUsername();
		logger.debug("Trying to create message by user with username '{}' for chat with id = {}",
				currentUsername, chatRoomId);
		
		ChatRoom room = getById(chatRoomId);
		User user = userService.getByUsername(currentUsername);
		
		ChatMessage message = new ChatMessage(text, LocalDateTime.now(), user, room);
		chatMessageDAO.saveOrUpdate(message);
		
		return message;
	}

	@Override
	public List<ChatMessage> getFirstPage(int chatId) throws ChatRoomNotFoundException, AccessDeniedException {
		String currentUsername = SecurityUtil.getCurrentUsername();
		logger.debug("Retrieving first page of chat messages from chat with id = {} for user with username = '{}'",
							chatId, currentUsername);
		
		//existence and access cheking
		getById(chatId);
		
		int amount = 20;		
		return chatMessageDAO.getFirstPage(amount, chatId);
	}

	@Override
	public List<ChatMessage> getPageBeforeEarliest(long earliestId, int chatId) throws ChatRoomNotFoundException, AccessDeniedException {
		String currentUsername = SecurityUtil.getCurrentUsername();
		logger.debug("Retrieving page of chat messages before earlieestId = {} from chat with id = {} for user with username = '{}'",
				earliestId, chatId, currentUsername);

		//existence and access cheking
		getById(chatId);
		
		int amount = 20;
		return chatMessageDAO.getPageBeforeEarliest(earliestId, amount, chatId);
	}
	
}
