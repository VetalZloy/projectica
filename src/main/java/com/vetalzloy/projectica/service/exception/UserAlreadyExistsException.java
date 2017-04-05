package com.vetalzloy.projectica.service.exception;

/**
 * This exception should be thrown when user with such fields already exists
 * @author VetalZloy
 *
 */
public class UserAlreadyExistsException extends Exception{

	private static final long serialVersionUID = -6066890589947609267L;

	public UserAlreadyExistsException(String message) {
		super(message);
	}
}
