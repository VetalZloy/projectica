package com.vetalzloy.projectica.service;

import java.util.List;

import com.vetalzloy.projectica.model.DialogMessage;
import com.vetalzloy.projectica.service.exception.UserNotFoundException;

/**
 * This interface provides methods for working with dialogs.
 * @author VetalZloy
 *
 */
public interface DialogService {	
	
	/**
	 * Creates dialog message in dialog between current user and 
	 * user with username {@code recieverUsername}. 
	 * If it was created successfuly, returns it. Else throws exception.
	 * @param text - text of dialog message
	 * @param recieverUsername - user who should get this message
	 * @return created message (never {@code null})
	 * @throws UserNotFoundException if current user is not registered
	 */
	DialogMessage create(String text, String recieverUsername) throws UserNotFoundException;
	
	/**
	 * Retrieves and returns first page of dialog message from dialog 
	 * between current user and user with username {@code interlocutorUsername}.
	 * If last message is unread by current user => makes it read.
	 * @param interlocutorUsername - message page will be retrieved from dialog 
	 * between current user and user with such username
	 * @return List of retrieved dialog messages ordered by sending date in descending order,
	 * can be empty, but never {@code null}
	 * @throws UserNotFoundException if current user or interlocutor is not registered.
	 */
	List<DialogMessage> getFirstPage(String interlocutorUsername) throws UserNotFoundException;
	
	/**
	 * Returns page of dialog messages which have ids lower than {@code earliestId} 
	 * from dialog between current user and user with username {@code interlocutorUsername}. 
	 * @param earliestId - id which must be higher than all ids in retrieved messages 
	 * and must be the closest to them.
	 * @param interlocutorUsername - message page will be retrieved from dialog 
	 * between current user and user with such username
	 * @return List of retrieved dialog messages ordered by sending date in descending order,
	 * can be empty, but never {@code null}
	 * @throws UserNotFoundException if current user or interlocutor is not registered.
	 */
	List<DialogMessage> getPageBeforeEarliest(long earliestId, String interlocutorUsername) throws UserNotFoundException;
	
	/**
	 * Returns id of dialog between current user and user with username {@code interlocutorUsername}
	 * @param interlocutorUsername - username of interlocutor of current user
	 * @return id of dialog in format "smallerNumber-biggerNumber". 
	 * Numbers are ids of current user and user with username {@code interlocutorUsername}.
	 * Examples: "1-23", "223-515"
	 * @throws UserNotFoundException if current user or interlocutor is not registered.
	 */
	String getDialogId(String interlocutorUsername) throws UserNotFoundException;
	
	/**
	 * Returns amount of messages which current user hasn't read
	 * @return amount of unread messages
	 * @throws UserNotFoundException if current user is not registered
	 */
	int getUnreadMessagesAmount() throws UserNotFoundException;

	/**
	 * Sets read field of {@code message} to {@code true} and updates message
	 * @param message - message will be updated
	 */
	void putRead(DialogMessage message);

	/**
	 * Sends message with congratulations from admin to current user, who is just registered
	 * @param justRegisteredUsername - username of just registered user, who will 
	 * receive message from admin
	 * @throws UserNotFoundException if admin or current user is not registered
	 */
	void sendAfterRegistrationMessage(String justRegisteredUsername) throws UserNotFoundException;
}
