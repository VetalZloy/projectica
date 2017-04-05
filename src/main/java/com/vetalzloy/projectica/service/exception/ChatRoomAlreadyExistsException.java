package com.vetalzloy.projectica.service.exception;

/**
 * This exception should be thrown when database already has chatroom with such name in such project
 * @author VetalZloy
 *
 */
public class ChatRoomAlreadyExistsException extends Exception{

	private static final long serialVersionUID = 7795112835210888966L;
	
	public ChatRoomAlreadyExistsException(String message) {
		super(message);
	}
	
}
