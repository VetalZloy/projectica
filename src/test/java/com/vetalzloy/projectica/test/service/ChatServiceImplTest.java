package com.vetalzloy.projectica.test.service;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.vetalzloy.projectica.model.ChatMessage;
import com.vetalzloy.projectica.model.ChatRoom;
import com.vetalzloy.projectica.model.Project;
import com.vetalzloy.projectica.model.User;
import com.vetalzloy.projectica.service.ChatService;
import com.vetalzloy.projectica.service.ProjectService;
import com.vetalzloy.projectica.service.exception.AccessDeniedException;
import com.vetalzloy.projectica.service.exception.ChatRoomAlreadyExistsException;
import com.vetalzloy.projectica.service.exception.ChatRoomNotFoundException;
import com.vetalzloy.projectica.service.exception.EntityNotFoundException;
import com.vetalzloy.projectica.service.exception.ProjectAlreadyExistsException;
import com.vetalzloy.projectica.service.exception.ProjectNotFoundException;
import com.vetalzloy.projectica.service.exception.UserNotFoundException;
import com.vetalzloy.projectica.test.configuration.ServiceConfiguration;
import com.vetalzloy.projectica.test.helper.DBHelper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=ServiceConfiguration.class, loader = AnnotationConfigContextLoader.class)
public class ChatServiceImplTest {
	
	@Autowired
	private ChatService chatService;
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private DBHelper helper;
	
	@Before
	public void cleaningDB(){
		helper.truncate();
		User user = helper.createDefaultUser();
		Authentication auth = new UsernamePasswordAuthenticationToken(user.getUsername(), "");
		SecurityContextHolder.getContext().setAuthentication(auth);
	}
	
	@Test
	public void getByIdTest() throws UserNotFoundException, ProjectAlreadyExistsException, ProjectNotFoundException, AccessDeniedException, ChatRoomAlreadyExistsException, ChatRoomNotFoundException{
		Project pr = projectService.createProject("name", "DBA", "desc");
		ChatRoom room1 = chatService.createChatRoom(pr.getId(), "new room");
		
		ChatRoom room2 = chatService.getById(room1.getId());
		assertTrue(room1.equals(room2));
	}
	
	@Test
	public void getFirstPageTest() throws ProjectAlreadyExistsException, AccessDeniedException, EntityNotFoundException{
		Project pr = projectService.createProject("name", "DBA", "desc");
		ChatRoom room = pr.getChatRooms().iterator().next();
		
		List<ChatMessage> firstPortion = new ArrayList<>();
		for(int i = 0; i < 20; i++)
			firstPortion.add(chatService.createMessage(room.getId(), "text #"+i));
		
		for(int i = 20; i < 40; i++)
			firstPortion.add(chatService.createMessage(room.getId(), "text #"+i));
		
		List<ChatMessage> firstPage = chatService.getFirstPage(room.getId());
		assertTrue(firstPortion.containsAll(firstPage));
	}
	
	@Test
	public void getPageBeforeEarliestTest() throws ProjectAlreadyExistsException, AccessDeniedException, EntityNotFoundException{
		Project pr = projectService.createProject("name", "DBA", "desc");
		ChatRoom room = pr.getChatRooms().iterator().next();
		
		List<ChatMessage> firstPortion = new ArrayList<>();
		for(int i = 0; i < 20; i++)
			firstPortion.add(chatService.createMessage(room.getId(), "text #"+i));
		
		ChatMessage outstangingMessage = chatService.createMessage(room.getId(), "waza");
		
		for(int i = 20; i < 40; i++)
			firstPortion.add(chatService.createMessage(room.getId(), "text #"+i));
		
		List<ChatMessage> page = chatService.getPageBeforeEarliest(outstangingMessage.getId(), 
																   room.getId());
		assertTrue(firstPortion.containsAll(page));
	}
	
	@Test
	public void createChatRoomTest() throws ProjectNotFoundException, AccessDeniedException, ChatRoomAlreadyExistsException, UserNotFoundException, ProjectAlreadyExistsException{
		Project pr = projectService.createProject("name", "DBA", "desc");
		ChatRoom room = chatService.createChatRoom(pr.getId(), "new room");
		
		pr = projectService.getFullById(pr.getId());
		assertTrue(pr.getChatRooms().contains(room));
	}
}
