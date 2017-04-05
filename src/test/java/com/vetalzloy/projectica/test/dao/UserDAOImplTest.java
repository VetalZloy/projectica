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

import com.vetalzloy.projectica.model.PasswordToken;
import com.vetalzloy.projectica.model.User;
import com.vetalzloy.projectica.model.VerificationToken;
import com.vetalzloy.projectica.service.dao.DialogMessageDAO;
import com.vetalzloy.projectica.service.dao.ProjectDAO;
import com.vetalzloy.projectica.service.dao.UserDAO;
import com.vetalzloy.projectica.test.configuration.DAOConfiguration;
import com.vetalzloy.projectica.test.helper.DBHelper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=DAOConfiguration.class)
public class UserDAOImplTest {
	
	//For normal transaction work
	@Autowired
	@Qualifier("userDAOProxy")
	private UserDAO userDAO;
	
	//For normal transaction work
	@Autowired
	@Qualifier("projectDAOProxy")
	private ProjectDAO projectDAO;
	
	//For normal transaction work
	@Autowired
	@Qualifier("dialogMessageDAOProxy")
	private DialogMessageDAO dialogMessageDAO;
	
	@Autowired
	private DBHelper helper;
	
	@Before
	public void cleaningDB(){
		helper.deleteAll();
	}
	
	@Test
	public void getByUsernameTest(){
		User u1 = new User("u1", "e1", "p1");
		userDAO.saveOrUpdate(u1);
		
		User u11 = userDAO.getByUsername(u1.getUsername());
		User u12 = userDAO.getByUsername(u1.getUsername().toUpperCase());	
		
		System.out.println(u11);
		System.out.println(u12);
		
		assertTrue(u1.equals(u11));
		assertTrue(! u11.equals(u12));
	}
	
	@Test
	public void saveAndGetsTest(){
		String username = "bill";
		String email = "bill@ya.ru";
		User user1 = new User(username, email, "12345");
		userDAO.saveOrUpdate(user1);
		
		User user2 = userDAO.getByEmail(email);
		assertTrue(user1.equals(user2));
		
		User user3 = userDAO.getByUsername(username);
		assertTrue(user1.equals(user3));
	}
	
	@Test
	public void verificationTokenSaveTest(){
		String username = "amanda";
		String email = "amanda@ya.ru";
		User user1 = new User(username, email, "12345");
		VerificationToken vt = new VerificationToken("tokn123", LocalDateTime.now());
		user1.setVerificationToken(vt);
		vt.setUser(user1);
		
		userDAO.saveOrUpdate(user1);
		User user2 = userDAO.getByVerificationToken(vt.getToken());
		assertTrue(user1.equals(user2));
	}
	
	@Test
	public void deleteUsersWithExpiredVerificationTokenTest(){
		String username1 = "jess";
		String email1 = "jess@ya.ru";
		User user1 = new User(username1, email1, "12345");
		VerificationToken vt1 = new VerificationToken("token1", LocalDateTime.now().minusDays(1));
		user1.setVerificationToken(vt1);
		vt1.setUser(user1);
		
		String username2 = "bonie";
		String email2 = "bonie@ya.ru";
		User user2 = new User(username2, email2, "12345");
		VerificationToken vt2 = new VerificationToken("token2", LocalDateTime.now().plusDays(1));
		user2.setVerificationToken(vt2);
		vt2.setUser(user2);
		
		userDAO.saveOrUpdate(user1);
		userDAO.saveOrUpdate(user2);
		
		userDAO.deleteUsersWithExpiredVerificationToken();
		
		User u1 = userDAO.getByEmail(email1);
		User u2 = userDAO.getByEmail(email2);
		assertTrue(u1 == null);
		assertTrue(u2.equals(user2));
	}

	@Test
	public void passwordTokenSaveTest(){
		String email = "logan@ya.ru";
		User user = new User("Logan", email, "12345");
		PasswordToken pt = new PasswordToken(user, "pt1");
		user.setPasswordToken(pt);
		
		userDAO.saveOrUpdate(user);
		User u = userDAO.getByPasswordToken(pt.getPasswordToken());
		assertTrue(user.equals(u));
	}

	@Test
	public void deletePasswordTokenTest(){
		String email = "megan@ya.ru";
		User user = new User("Megan", email, "12345");
		PasswordToken pt = new PasswordToken(user, "megan1");
		user.setPasswordToken(pt);
		
		userDAO.saveOrUpdate(user);
		User u = userDAO.getByEmail(email);
		userDAO.deletePasswordToken(u);
		
		User u1 = userDAO.getByEmail(email);
		assertTrue(u1.getPasswordToken() == null);
	}

	@Test
	public void deleteExpiredPasswordTokensTest(){
		String email1 = "aidra@ya.ru";
		User user1 = new User("Aidra", email1, "12345");
		PasswordToken pt1 = new PasswordToken(user1, "aidra1");
		pt1.setExpireDate(LocalDateTime.now().minusDays(1));
		user1.setPasswordToken(pt1);
		
		String email2 = "lola@ya.ru";
		User user2 = new User("Lola", email2, "12345");
		PasswordToken pt2 = new PasswordToken(user2, "lola1");
		pt2.setExpireDate(LocalDateTime.now().plusDays(1));
		user2.setPasswordToken(pt2);
		
		userDAO.saveOrUpdate(user1);
		userDAO.saveOrUpdate(user2);
		userDAO.deleteExpiredPasswordTokens();
		
		User u1 = userDAO.getByPasswordToken(pt1.getPasswordToken());
		User u2 = userDAO.getByPasswordToken(pt2.getPasswordToken());
		
		assertTrue(u1 == null);
		assertTrue(user2.equals(u2));

	}
	
	@Test
	public void getByVerificationTokenTest(){
		User user = new User("bill", "bill@ya.ru", "12345");
		VerificationToken vt = new VerificationToken("token1", LocalDateTime.now(), user);
		user.setVerificationToken(vt);
		
		userDAO.saveOrUpdate(user);
		
		User user2 = userDAO.getByVerificationToken(vt.getToken());
		
		assertTrue(user.equals(user2));		
	}

	@Test
	public void getByPasswordTokenTest(){
		User user = new User("bill", "bill@ya.ru", "12345");
		PasswordToken pt = new PasswordToken(user, "passwordToken1");
		user.setPasswordToken(pt);
		
		userDAO.saveOrUpdate(user);
		User user2 = userDAO.getByPasswordToken(pt.getPasswordToken());
		
		assertTrue(user.equals(user2));
	}
	
	@Test
	public void getUsersPageTest(){
		
		List<User> users = new ArrayList<>();
		for(int i = 1; i < 6; i++) {
			User u = new User(i+"", i+"@ya.ru", i+"");
			u.setEnabled(true);
			users.add(u);
		}
		
		users.forEach(userDAO::saveOrUpdate);
		
		List<User> page = userDAO.getUsersPage(1, 3);
		
		assertTrue(page.contains(users.get(1)));
		assertTrue(page.contains(users.get(2)));
		assertTrue(page.contains(users.get(3)));
	}
	
}
