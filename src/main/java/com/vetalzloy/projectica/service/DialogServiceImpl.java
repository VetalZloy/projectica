package com.vetalzloy.projectica.service;

import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.vetalzloy.projectica.model.DialogMessage;
import com.vetalzloy.projectica.model.Interlocutor;
import com.vetalzloy.projectica.model.User;
import com.vetalzloy.projectica.service.dao.DialogMessageDAO;
import com.vetalzloy.projectica.service.exception.UserNotFoundException;
import com.vetalzloy.projectica.util.SecurityUtil;

@Service
@Transactional
public class DialogServiceImpl implements DialogService {

	private static final Logger logger = LoggerFactory.getLogger(DialogServiceImpl.class);
	
	@Autowired
	private DialogMessageDAO dialogMessageDAO;
	
	@Autowired
	private UserService userService;	

	@Autowired
	@Qualifier("message")
	private String message;
	
	@Override
	public List<DialogMessage> getFirstPage(String interlocutorUsername) 
												throws UserNotFoundException {
		int amount = 20;
		String currentUsername = SecurityUtil.getCurrentUsername();
		logger.debug("Retrieving first messages page for users with usernames: {} and {} in amount of {}",
				currentUsername, interlocutorUsername, amount);
		
		User u1 = userService.getByUsername(currentUsername);
		User u2 = userService.getByUsername(interlocutorUsername);
		
		List<DialogMessage> messages = dialogMessageDAO.getFirstPage(amount, 
																	 u1.getUserId(), 
																	 u2.getUserId());
		
		//If the first page is extracted and receiver 
		//of the last message is current user => last message will be read any way
		if(messages.size() > 0 ) {
			String lastMessageReciever = messages.get(0).getReciever().getUsername();
			if(currentUsername.equals(lastMessageReciever))
				messages.get(0).setRead(true);
		}
		
		return messages;
	}

	@Override
	public List<DialogMessage> getPageBeforeEarliest(long earliestId, String u2) 
														throws UserNotFoundException {
		String u1 = SecurityUtil.getCurrentUsername();		
		int amount = 20;
		User user1 = userService.getByUsername(u1);
		User user2 = userService.getByUsername(u2);
		logger.debug("Retrieving messages before earliest with id = {} for users with usernames: {} and {} in amount of {} ...",
				earliestId, u1, u2, amount);
		
		return dialogMessageDAO.getPageBeforeEarliest(earliestId, 
													  amount, 
													  user1.getUserId(), 
													  user2.getUserId());
	}

	@Override
	public DialogMessage create(String text, String recieverUsername) throws UserNotFoundException {	
		return create(text, SecurityUtil.getCurrentUsername(), recieverUsername);
	}
	
	private DialogMessage create(String text, String senderUsername, String recieverUsername) 
									throws UserNotFoundException {
		
		//HARD business logic, to be very attentive in case of changing something
		
		logger.debug("Creating message: sender's username - '{}', reciever's username - '{}' ...",
				senderUsername, recieverUsername);
		
		//Extracting sender and reciever
		User sender = userService.getByUsername(senderUsername);
		User reciever = userService.getByUsername(recieverUsername);
		
		//Create and save dialog message
		DialogMessage message = new  DialogMessage(text, LocalDateTime.now(), sender, reciever);
		
		logger.debug("Saving dialog message {} ...", message);		
		dialogMessageDAO.saveOrUpdate(message);
		
		//Initialize interlocutors for sender and receiver respectively
		userService.loadInterlocutors(sender);
		userService.loadInterlocutors(reciever);
		
		/*
		 * Hard part. Extract interlocutors for sender: if reciever among them => return him,
		 * else - create new Interlocutor instance this subject field - sender and 
		 * interlocutor field - reciever
		 */
		Interlocutor senderRecieverInterlocutor = sender
												  .getInterlocutors()
												  .stream()
												  .filter(i -> i.getInterlocutor().equals(reciever))
												  .findFirst()
												  .orElse(new Interlocutor(sender, reciever));
		
		/*
		 * Vise versa. Extract interlocutors for reciver: if sender among them => return him,
		 * else - create new Interlocutor instance this subject field - reciever and 
		 * interlocutor field - sender
		 */
		Interlocutor recieverSenderInterlocutor = reciever
												  .getInterlocutors()
												  .stream()
												  .filter(i -> i.getInterlocutor().equals(sender))
												  .findFirst()
												  .orElse(new Interlocutor(reciever, sender));
		
		/*
		 * If such interlocutors already exist => replace current last message by just created one,
		 * else just set just created message
		 */		
		senderRecieverInterlocutor.setLastDialogMessage(message);
		recieverSenderInterlocutor.setLastDialogMessage(message);
		
		/*
		 * Add Interlocutor instances to sender and reciever respectively.
		 * If sender and reciever already have such interlocutors, nothing will change,
		 * else new Interlocutor instances will be added
		 */
		sender.getInterlocutors().add(senderRecieverInterlocutor);
		reciever.getInterlocutors().add(recieverSenderInterlocutor);
		
		/*
		 * Before closing transaction users data and, thereby, 
		 * interlocutors(new or changed) will be saved.
		 */
		
		return message;
	}
	
	@Override
	public String getDialogId(String interlocutorUsername) throws UserNotFoundException {
		String currentUsername = SecurityUtil.getCurrentUsername();	
		
		User a = userService.getByUsername(currentUsername);
		User b = userService.getByUsername(interlocutorUsername);
		if(a.getUserId() < b.getUserId()) return a.getUserId() + "-" + b.getUserId();		
		else return b.getUserId() + "-" + a.getUserId();
	}

	@Override
	public int getUnreadMessagesAmount() throws UserNotFoundException {
		//no logging because it will be invoked very often
		
		String currentUsername = SecurityUtil.getCurrentUsername();
		
		User user = userService.getByUsername(currentUsername);
		
		long unreadMessagesAmount = user.getInterlocutors()
										.stream()
										.map(i -> i.getLastDialogMessage())
										.filter(m -> !m.isRead())
										.map(m -> m.getReciever().getUsername())
										.filter(u -> u.equals(currentUsername))
										.count();
		
		return (int) unreadMessagesAmount;
	}

	@Override
	public void putRead(DialogMessage message) {
		
		/*
		 * not very cool checking, means "if message has never been saved" => doesn't exist,
		 * may be rewritten in time
		 */
		if(message.getMessageId() == 0) return;
		
		logger.debug("Setting read field to true for dialog message {}", message);
		
		message.setRead(true);		
		dialogMessageDAO.saveOrUpdate(message);
		
	}

	@Override
	public void sendAfterRegistrationMessage(String justRegisteredUsername) throws UserNotFoundException {
		create(message, "VetalZloy", justRegisteredUsername);
	}

}
