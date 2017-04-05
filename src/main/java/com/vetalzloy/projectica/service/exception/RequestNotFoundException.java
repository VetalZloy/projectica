package com.vetalzloy.projectica.service.exception;

/**
 * This exception should be thrown when necessary request doesn't exist
 * @author VetalZloy
 *
 */
public class RequestNotFoundException extends EntityNotFoundException {

	private static final long serialVersionUID = -6969091466987986023L;

	public RequestNotFoundException(String string) {
		super(string);
	}

	public RequestNotFoundException(long positionId, String username) {
		super("Request with positionId = " + positionId + 
				" and username = " + username + " not exists");
	}
}
