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

import com.vetalzloy.projectica.model.DialogMessage;
import com.vetalzloy.projectica.model.User;
import com.vetalzloy.projectica.service.DialogService;
import com.vetalzloy.projectica.service.UserService;
import com.vetalzloy.projectica.service.exception.UserNotFoundException;
import com.vetalzloy.projectica.test.configuration.ServiceConfiguration;
import com.vetalzloy.projectica.test.helper.DBHelper;
import com.vetalzloy.projectica.util.SecurityUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=ServiceConfiguration.class, loader = AnnotationConfigContextLoader.class)
public class DialogServiceImplTest {

	@Autowired
	private DialogService dialogService;
	
	@Autowired
	private UserService userService;
	
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
	public void getFirstPageTest() throws UserNotFoundException{
		String vetalZloy = SecurityUtil.getCurrentUsername();
		User will = helper.createUser("William", "william@gmail.com", "111");
		for(int i = 0; i < 30; i++)
			dialogService.create("message #"+i, will.getUsername());
		
		List<DialogMessage> unreadMessages = dialogService.getFirstPage(will.getUsername());
		
		setCurrentUsername(will.getUsername());
		List<DialogMessage> readMessages = dialogService.getFirstPage(vetalZloy);
		
		assertTrue(unreadMessages.size() == readMessages.size() && 
				   readMessages.size() == 20 &&
				   !unreadMessages.get(0).isRead() && 
				   readMessages.get(0).isRead());
		
	}
	
	@Test
	public void getPageBeforeEarliestTest() throws UserNotFoundException{
		User will = helper.createUser("William", "william@gmail.com", "111");
		List<DialogMessage> firstPortion = new ArrayList<>();
		for(int i = 0; i < 20; i++)
			firstPortion.add(dialogService.create("message #"+i, will.getUsername()));
		
		DialogMessage outstandingMessage = dialogService.create("outstanding message", will.getUsername());
		
		for(int i = 20; i < 40; i++)
			dialogService.create("message #"+i, will.getUsername());
		
		List<DialogMessage> beforeEarliest = dialogService.getPageBeforeEarliest(outstandingMessage.getMessageId(), 
																				 will.getUsername());
		
		assertTrue(beforeEarliest.containsAll(firstPortion) && 
				   beforeEarliest.size() == firstPortion.size());
	}
	
	@Test
	public void getDialogIdTest() throws UserNotFoundException{
		User will = helper.createUser("William", "william@gmail.com", "111");
		User vetalZloy = userService.getByUsername(SecurityUtil.getCurrentUsername());
		
		String dialogId = vetalZloy.getUserId() + "-" + will.getUserId();
		
		dialogService.create("message", will.getUsername());
		
		String generatedDialogId = dialogService.getDialogId(will.getUsername());
		assertTrue(dialogId.equals(generatedDialogId));		
	}
	
	@Test
	public void getUnreadMessagesAmountTest() throws UserNotFoundException{
		String vetalZloy = SecurityUtil.getCurrentUsername();
		
		User will = helper.createUser("William", "william@gmail.com", "111");
		setCurrentUsername(will.getUsername());
		dialogService.create("message1", vetalZloy);
		
		User bob = helper.createUser("Bob", "bob@gmail.com", "111");
		setCurrentUsername(bob.getUsername());
		dialogService.create("message2", vetalZloy);
		
		setCurrentUsername(vetalZloy);
		assertTrue(dialogService.getUnreadMessagesAmount() == 2);
	}
	
	@Test
	public void sendAfterRegistrationMessageTest() throws UserNotFoundException{
		User will = helper.createUser("William", "william@gmail.com", "111");
		dialogService.sendAfterRegistrationMessage(will.getUsername());
		
		setCurrentUsername(will.getUsername());
		assertTrue(dialogService.getUnreadMessagesAmount() == 1);
		
	}
	
	@Test
	public void putReadTest() throws UserNotFoundException{
		User will = helper.createUser("William", "william@gmail.com", "111");
		DialogMessage message = dialogService.create("message", will.getUsername());
		dialogService.putRead(message);
		assertTrue(message.isRead());		
	}
	
	@Test(expected=UserNotFoundException.class)
	public void createMessageTest() throws UserNotFoundException{
		dialogService.create("text", "unregisteredUser");
	}
}
