package com.vetalzloy.projectica.service.exception;

/**
 * This exception should be thrown when necessary passwordToken doesn't exist
 * @author VetalZloy
 *
 */
public class PasswordTokenNotFoundException extends EntityNotFoundException {

	private static final long serialVersionUID = 218787517263781908L;

	public PasswordTokenNotFoundException(String string){
		super(string);
	}
	
}
