package com.vetalzloy.projectica.service.exception;

/**
 * Abstract class for exceptions which show unexistence of an entity
 * @author VetalZloy
 *
 */
public abstract class EntityNotFoundException extends Exception{

	private static final long serialVersionUID = 4828460192600435018L;
	
	public EntityNotFoundException(String string){
		super(string);
	}
	
}
