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

import com.vetalzloy.projectica.model.DialogMessage;
import com.vetalzloy.projectica.model.User;
import com.vetalzloy.projectica.service.dao.DialogMessageDAO;
import com.vetalzloy.projectica.service.dao.UserDAO;
import com.vetalzloy.projectica.test.configuration.DAOConfiguration;
import com.vetalzloy.projectica.test.helper.DBHelper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=DAOConfiguration.class)
public class DialogMessageDAOImplTest {
	
	@Autowired
	@Qualifier("dialogMessageDAOProxy")
	private DialogMessageDAO dialogMessageDAO;
	
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
	public void getFirstPageTest(){
		User u1 = new User("u1", "e1", "p1");
		User u2 = new User("u2", "e2", "p2");
		User u3 = new User("u3", "e3", "p3");
		
		/* for reducing queries amount to DB need to save users before messages ==> 
		cascading in @ManyToOne mapping will be needless ==> amount of queries will reduce */
		userDAO.saveOrUpdate(u1);
		userDAO.saveOrUpdate(u2);
		userDAO.saveOrUpdate(u3);
		
		DialogMessage m1 = new DialogMessage("text1", LocalDateTime.now(), u1, u2);
		DialogMessage m2 = new DialogMessage("text2", LocalDateTime.now(), u2, u1);
		DialogMessage m3 = new DialogMessage("text3", LocalDateTime.now(), u1, u2);
		DialogMessage m4 = new DialogMessage("text4", LocalDateTime.now(), u2, u3);
		
		dialogMessageDAO.saveOrUpdate(m1);		
		dialogMessageDAO.saveOrUpdate(m2);
		dialogMessageDAO.saveOrUpdate(m3);		
		dialogMessageDAO.saveOrUpdate(m4);
		
		List<DialogMessage> messages = dialogMessageDAO.getFirstPage(10, u1.getUserId(), u2.getUserId());
		assertTrue(messages.size() == 3);
	}
	
	@Test
	public void getPageBeforeEarliest(){
		User u1 = new User("u1", "e1", "p1");
		User u2 = new User("u2", "e2", "p2");
		
		/* for reducing queries amount to DB need to save users before messages ==> 
		cascading in @ManyToOne mapping will be needless ==> amount of queries will reduce */
		userDAO.saveOrUpdate(u1);
		userDAO.saveOrUpdate(u2);
		
		List<DialogMessage> list = new ArrayList<>();
		LocalDateTime date = LocalDateTime.now();
		
		for(int i = 0; i < 30; i++){
			DialogMessage m = new DialogMessage("text"+(i+1), date.plusSeconds(i+1), u1, u2);
			list.add(m);
		}
		
		for(DialogMessage m: list)
			dialogMessageDAO.saveOrUpdate(m);
		
		int earliestNumber = 9;
		long earliestId = list.get(earliestNumber).getMessageId();
		List<DialogMessage> resultList = dialogMessageDAO.getPageBeforeEarliest(earliestId, 50, u1.getUserId(), u2.getUserId());
		assertTrue(resultList.size() == earliestNumber);
		assertTrue(list.containsAll(resultList));
	}
}
