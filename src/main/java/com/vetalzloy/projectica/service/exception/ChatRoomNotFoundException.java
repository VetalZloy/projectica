package com.vetalzloy.projectica.service.exception;

/**
 * This exception should be thrown when necessary chatRoom doesn't exist
 * @author VetalZloy
 *
 */
public class ChatRoomNotFoundException extends EntityNotFoundException {

	private static final long serialVersionUID = -4011129366134383967L;

	public ChatRoomNotFoundException(String string) {
		super(string);
	}
	
	public ChatRoomNotFoundException(int id) {
		super("ChatRoom with id = " + id + " are not found");
	}

}
