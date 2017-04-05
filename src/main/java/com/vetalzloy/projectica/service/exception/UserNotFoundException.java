package com.vetalzloy.projectica.service.exception;

/**
 * This exception should be thrown when necessary user doesn't exist
 * @author VetalZloy
 *
 */
public class UserNotFoundException extends EntityNotFoundException {

	public UserNotFoundException(String string) {
		super(string);
	}

	private static final long serialVersionUID = -4365124797410904234L;

}
