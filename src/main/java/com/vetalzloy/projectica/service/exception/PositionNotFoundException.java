package com.vetalzloy.projectica.service.exception;

/**
 * This exception should be thrown when necessary position doesn't exist
 * @author VetalZloy
 *
 */
public class PositionNotFoundException extends EntityNotFoundException {

	private static final long serialVersionUID = 6969438029728977702L;
	
	public PositionNotFoundException(String string){
		super(string);
	}
	
	public PositionNotFoundException(long positionId){
		super("Position with id = " + positionId + " doesn't exist.");
	}
}
