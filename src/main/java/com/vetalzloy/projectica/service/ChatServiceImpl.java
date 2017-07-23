package com.vetalzloy.projectica.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vetalzloy.projectica.model.ChatRoom;
import com.vetalzloy.projectica.model.Project;
import com.vetalzloy.projectica.model.User;
import com.vetalzloy.projectica.service.dao.ChatRoomDAO;
import com.vetalzloy.projectica.service.exception.AccessDeniedException;
import com.vetalzloy.projectica.service.exception.ChatRoomAlreadyExistsException;
import com.vetalzloy.projectica.service.exception.ChatRoomNotFoundException;
import com.vetalzloy.projectica.service.exception.ExternalResourceAccessException;
import com.vetalzloy.projectica.service.exception.ProjectNotFoundException;
import com.vetalzloy.projectica.util.SecurityUtil;

@Service
@Transactional
public class ChatServiceImpl implements ChatService {

	private static final Logger logger = LoggerFactory.getLogger(ChatServiceImpl.class);
	
	@Autowired
	private ChatRoomDAO chatRoomDAO;
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private MessagingService messagingService;
	
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
	public ChatRoom createChatRoom(int projectId, String name) throws AccessDeniedException, ProjectNotFoundException, ChatRoomAlreadyExistsException, ExternalResourceAccessException {
		String currentUsername = SecurityUtil.getCurrentUsername();
		Project project = projectService.getById(projectId);
		
		//checking whether chat room with such name exists
		boolean existance = project.getChatRooms()
								   .stream()
								   .anyMatch(r -> r.getName().equals(name));
		if(existance) throw new ChatRoomAlreadyExistsException("Chatroom with name = " + name + 
															  " in project with id = " + projectId + 
															  " already exists.");
		
		//checking whether current user is the creator of appropriate project
		if(! project.getCreator().getUsername().equals(currentUsername))
			throw new AccessDeniedException("Trying to create chatroom for project with id = "
											+ projectId + " by user with username = " + currentUsername);
		
		ChatRoom room = new ChatRoom(name, project);
		chatRoomDAO.saveOrUpdate(room);
		
		User[] users = project.getPositions()
								.stream()
								.filter(p -> p.getFiringDate() == null && 
											 p.getHiringDate() != null)
								.map(p -> p.getUser())
								.toArray(User[]::new);
		messagingService.addUsersToChatRoom(room, users);
		return room;
	}

	@Override
	public void addUsersToChatRoom(ChatRoom room, User... users) throws ExternalResourceAccessException {
		messagingService.addUsersToChatRoom(room, users);
	}

	@Override
	public void removeUsersFromChatRoom(ChatRoom room, User... users) throws ExternalResourceAccessException {
		messagingService.removeUsersFromChatRoom(room, users);
	}
	
}
