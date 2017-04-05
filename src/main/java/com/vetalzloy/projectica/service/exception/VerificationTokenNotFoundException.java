package com.vetalzloy.projectica.service.exception;

/**
 * This exception should be thrown when necessary verificationToken doesn't exist
 * @author VetalZloy
 *
 */
public class VerificationTokenNotFoundException extends EntityNotFoundException {

	private static final long serialVersionUID = 1152724046189840661L;
	
	public VerificationTokenNotFoundException(String string){
		super(string);
	}
	
}
