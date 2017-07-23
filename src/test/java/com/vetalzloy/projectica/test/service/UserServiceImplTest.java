package com.vetalzloy.projectica.test.service;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

import com.vetalzloy.projectica.model.PasswordToken;
import com.vetalzloy.projectica.model.Position;
import com.vetalzloy.projectica.model.Project;
import com.vetalzloy.projectica.model.User;
import com.vetalzloy.projectica.service.ProjectService;
import com.vetalzloy.projectica.service.TagService;
import com.vetalzloy.projectica.service.UserService;
import com.vetalzloy.projectica.service.exception.ExternalResourceAccessException;
import com.vetalzloy.projectica.service.exception.PasswordTokenNotFoundException;
import com.vetalzloy.projectica.service.exception.ProjectAlreadyExistsException;
import com.vetalzloy.projectica.service.exception.UserAlreadyExistsException;
import com.vetalzloy.projectica.service.exception.UserNotFoundException;
import com.vetalzloy.projectica.test.configuration.ServiceConfiguration;
import com.vetalzloy.projectica.test.helper.DBHelper;
import com.vetalzloy.projectica.util.SecurityUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=ServiceConfiguration.class, loader = AnnotationConfigContextLoader.class)
public class UserServiceImplTest {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private TagService tagService;
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private DBHelper helper;
	
	private void setCurrentUsername(String username){
		Authentication auth = new UsernamePasswordAuthenticationToken(username, "");
		SecurityContextHolder.getContext().setAuthentication(auth);
	}
	
	@Before
	public void cleaningDB(){
		helper.truncate();
		User user = helper.createDefaultUser();
		Authentication auth = new UsernamePasswordAuthenticationToken(user.getUsername(), "");
		SecurityContextHolder.getContext().setAuthentication(auth);
	}
	
	@Test
	public void getSimilarsTest() throws UserNotFoundException{
		User u1 = helper.createUser("u1", "email", "111");
		for(int i = 0; i < 10; i++) 
			helper.createUser("user"+i, "email", "111");
		User u2 = helper.createUser("u2", "email", "111");
		
		setCurrentUsername(u1.getUsername());
		tagService.attachToUser("MySQL");
		tagService.attachToUser("Java");
		
		setCurrentUsername(u2.getUsername());
		tagService.attachToUser("MySQL");
		tagService.attachToUser("Java");
		tagService.attachToUser("C++");
		
		List<User> similarUsers = userService.getSimilars("", Arrays.asList("Java", "MySQL"));
		assertTrue(similarUsers.size() == 2 && 
				   similarUsers.contains(u1) && 
				   similarUsers.contains(u2));
	}
	
	@Test
	public void getUsersPageTest(){
		List<User> users = new ArrayList<>();
		for(int i = 0; i < 30; i++)
			users.add(helper.createUser("user #"+i, "email", "password"));
		
		/*
		 * Page size = 25; 0 page - 1..25, 1 page - 25..50
		 * We have 30 users + default user => 31
		 */		
		List<User> usersPage = userService.getUsersPage(1);
		assertTrue(usersPage.size() == 6 && users.containsAll(usersPage));
	}
	
	@Test
	public void getByIdTest() throws UserNotFoundException{
		User u1 = helper.createUser("u1", "email", "111");
		User u2 = userService.getById(u1.getUserId());
		
		assertTrue(u1.equals(u2));
	}
	
	@Test
	public void getByUsernameTest() throws UserNotFoundException{
		User u1 = helper.createUser("u1", "email", "111");
		for(int i = 0; i < 5; i++)
			helper.createUser("user #"+i, "email", "111");
		
		User u2 = userService.getByUsername(u1.getUsername());
		assertTrue(u1.equals(u2));
	}
	
	@Test
	public void getFullByUsername() throws UserNotFoundException, ProjectAlreadyExistsException, ExternalResourceAccessException{
		User u1 = helper.createUser("u1", "email", "111");
		setCurrentUsername(u1.getUsername());
		Project pr = projectService.createProject("Super project", "DBA", "description");
		Position pos = pr.getPositions().iterator().next();

		List<String> tags = Arrays.asList("MongoDB", "Java");
		for(String tag: tags)
			tagService.attachToUser(tag);
		
		User u2 = userService.getFullByUsername(u1.getUsername());
		List<String> u2Tags = u2.getTags().stream()
										  .map(t -> t.getTag())
										  .collect(Collectors.toList());
		
		assertTrue(u1.equals(u2) && 
				   u2.getCreatedProjects().contains(pr) && 
				   u2.getPositions().contains(pos) && 
				   tags.containsAll(u2Tags));
	}
	
	@Test
	public void getByEmailTest() throws UserNotFoundException{
		User u1 = helper.createUser("u1", "email1", "111");
		for(int i = 0; i < 5; i++)
			helper.createUser("user #"+i, "long-email #"+i, "111");
		
		User u2 = userService.getByEmail(u1.getEmail());
		assertTrue(u1.equals(u2));
	}
	
	@Test
	public void updateTest() throws UserNotFoundException{
		String name = "Vitaly";
		String surname = "Dumma";
		String cvLink = "https://vk.com";
		userService.update(name, surname, cvLink);
		User u = userService.getByUsername(SecurityUtil.getCurrentUsername());
		
		assertTrue(u.getName().equals(name) && 
				   u.getSurname().equals(surname) && 
				   u.getCvLink().equals(cvLink));
	}
	
	@Test
	public void changePasswordTest() throws UserNotFoundException, PasswordTokenNotFoundException{
		User u = userService.getByUsername(SecurityUtil.getCurrentUsername());
		PasswordToken pt = userService.createPasswordToken(u.getUsername());
		userService.changePassword(pt.getPasswordToken(), "12345");
		
		User u2 = userService.getByUsername(u.getUsername());
		assertTrue(! u2.getPassword().equals(u.getPassword()));
	}
	
	@Test
	public void registerTest() throws UserAlreadyExistsException, UserNotFoundException {
		String username = "username";
		String email = "email";
		userService.register(username, email, "111");
		User u = userService.getByUsername(username);
		assertTrue(u.getUsername().equals(username) && 
				   u.getEmail().equals(email) && 
				   !u.isEnabled());
	}
	
	@Test
	public void isExistsTest() throws UserNotFoundException{
		User u = userService.getByUsername(SecurityUtil.getCurrentUsername());
		
		assertTrue(userService.isExists(u.getUsername()) && 
				   userService.isExists(u.getEmail()));
	}
}
