package com.vetalzloy.projectica.service.exception;

/**
 * This exception should be thrown when necessary tag doesn't exist
 * @author VetalZloy
 *
 */
public class TagNotFoundException extends EntityNotFoundException {

	private static final long serialVersionUID = 2652033352369131372L;

	public TagNotFoundException(String string) {
		super(string);
	}

}
