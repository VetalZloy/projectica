package com.vetalzloy.projectica.test.dao;

import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.vetalzloy.projectica.model.ChatMessage;
import com.vetalzloy.projectica.model.ChatRoom;
import com.vetalzloy.projectica.model.Project;
import com.vetalzloy.projectica.model.User;
import com.vetalzloy.projectica.service.dao.ChatMessageDAO;
import com.vetalzloy.projectica.service.dao.ChatRoomDAO;
import com.vetalzloy.projectica.service.dao.ProjectDAO;
import com.vetalzloy.projectica.service.dao.UserDAO;
import com.vetalzloy.projectica.test.configuration.DAOConfiguration;
import com.vetalzloy.projectica.test.helper.DBHelper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=DAOConfiguration.class, loader = AnnotationConfigContextLoader.class)
public class ChatMessageDAOImplTest {
	
	@Autowired
	@Qualifier("chatRoomDAOProxy")
	private ChatRoomDAO chatRoomDAO;
	
	@Autowired
	@Qualifier("chatMessageDAOProxy")
	private ChatMessageDAO chatMessageDAO;
	
	@Autowired
	@Qualifier("projectDAOProxy")
	private ProjectDAO projectDAO;
	
	@Autowired
	@Qualifier("userDAOProxy")
	private UserDAO userDAO;
	
	@Autowired
	private DBHelper helper;
		
	@Before
	public void cleaningDB(){
		helper.deleteAll();
	}
	
	@Test
	public void gettersTest(){		
		LocalDateTime now = LocalDateTime.now();
		
		User user = new User("u1", "e1", "p1");
		
		Project p = new Project("name", "descripription", now, user);
		projectDAO.saveOrUpdate(p);
		
		ChatRoom room = new ChatRoom("room1", p);
		p.getChatRooms().add(room);
		
		projectDAO.saveOrUpdate(p);
		
		List<ChatMessage> messages = new ArrayList<>();
		for(int i = 0; i < 25; i++)
			messages.add( new ChatMessage(""+i, now.plusSeconds(i), user, room));
		
		for(ChatMessage message: messages)
			chatMessageDAO.saveOrUpdate(message);
		
		List<ChatMessage> firstPage = chatMessageDAO.getFirstPage(10, room.getId());
		
		assertTrue(messages.containsAll(firstPage));
		
		ChatMessage latestMessage = messages.get(messages.size()-1);
		assertTrue(firstPage.contains(latestMessage));
		
		
		List<ChatMessage> list1 = chatMessageDAO.getPageBeforeEarliest(messages.get(5).getId(),
																	   10, 
																	   room.getId());
		latestMessage = list1.get(list1.size()-1);
		
		assertTrue(list1.size() == 5);
		assertTrue(latestMessage.equals(messages.get(0)));
	}
	
}
