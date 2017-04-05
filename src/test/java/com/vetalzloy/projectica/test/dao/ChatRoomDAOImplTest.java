package com.vetalzloy.projectica.test.dao;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.vetalzloy.projectica.model.ChatRoom;
import com.vetalzloy.projectica.model.Project;
import com.vetalzloy.projectica.service.dao.ChatRoomDAO;
import com.vetalzloy.projectica.service.dao.ProjectDAO;
import com.vetalzloy.projectica.test.configuration.DAOConfiguration;
import com.vetalzloy.projectica.test.helper.DBHelper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=DAOConfiguration.class)
public class ChatRoomDAOImplTest {
	
	@Autowired
	@Qualifier("chatRoomDAOProxy")
	private ChatRoomDAO chatRoomDAO;
	
	@Autowired
	@Qualifier("projectDAOProxy")
	private ProjectDAO projectDAO;
	
	@Autowired
	private DBHelper helper;
		
	@Before
	public void cleaningDB(){
		helper.deleteAll();
	}
	
	@Test
	public void getByIdTest(){		
		Project p = new Project("aaa");
		projectDAO.saveOrUpdate(p);
		
		List<ChatRoom> rooms = new ArrayList<>();
		for(int i = 0; i < 10; i++){
			ChatRoom room = new ChatRoom("room ¹" + (i), p);
			p.getChatRooms().add(room);
			rooms.add(room);
		}	
			
		projectDAO.saveOrUpdate(p);
		
		ChatRoom room1 = chatRoomDAO.getById(rooms.get(0).getId());
		ChatRoom room2 = chatRoomDAO.getById(100000);
		
		assertTrue(rooms.get(0).equals(room1));
		assertTrue(room2 == null);
	}
	
}
