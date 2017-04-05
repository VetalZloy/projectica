package com.vetalzloy.projectica.service.exception;

/**
 * This exception should be thrown when necessary project doesn't exist
 * @author VetalZloy
 *
 */
public class ProjectNotFoundException extends EntityNotFoundException {

	private static final long serialVersionUID = 4333038268646936712L;

	public ProjectNotFoundException(String string) {
		super(string);
	}
	
	public ProjectNotFoundException(int projectId){
		super("Project with id = " + projectId + " doesn't exist.");
	}

}
