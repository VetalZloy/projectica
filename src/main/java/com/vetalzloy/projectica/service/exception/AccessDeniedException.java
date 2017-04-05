package com.vetalzloy.projectica.service.exception;

/**
 * This exception should be thrown when user tries to get access to things, 
 * which are not belong to him
 * @author VetalZloy
 *
 */
public class AccessDeniedException extends Exception {

	private static final long serialVersionUID = -8796583942345830627L;
	
	public AccessDeniedException(String msg){
		super(msg);
	}
	
}
