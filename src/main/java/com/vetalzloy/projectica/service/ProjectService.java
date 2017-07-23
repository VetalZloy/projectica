package com.vetalzloy.projectica.service;

import java.util.List;

import com.vetalzloy.projectica.model.Project;
import com.vetalzloy.projectica.service.exception.AccessDeniedException;
import com.vetalzloy.projectica.service.exception.EntityNotFoundException;
import com.vetalzloy.projectica.service.exception.ExternalResourceAccessException;
import com.vetalzloy.projectica.service.exception.ProjectAlreadyExistsException;
import com.vetalzloy.projectica.service.exception.ProjectNotFoundException;
import com.vetalzloy.projectica.service.exception.UserNotFoundException;

/**
 * This interface provides methods for working with projects
 * @author VetalZloy
 *
 */
public interface ProjectService {
	
	/**
	 * Returns page of projects without "LAZY" collections. 
	 * Amount of retrieved projects is under control of implementation
	 * @param page - number of requested page.
	 * @return List of projects, can be empty, but never {@code null}
	 */
	List<Project> getProjectsPage(int page);
	
	/**
	 * Returns project with such {@code projectId}. Without "LAZY" collections
	 * @param projectId - id of requested project
	 * @return retrieved project with such {@code projectId} (never {@code null})
	 * @throws ProjectNotFoundException if project with such {@code projectId} doesn't exist
	 * @see getFullById(int projectId)
	 */
	Project getById(int projectId) throws ProjectNotFoundException;
	
	/**
	 * Returns project with such {@code projectId} and loads all "LAZY" collections
	 * @param projectId - id of requested project
	 * @return retrieved project with such {@code projectId} and 
	 * loaded "LAZY" collections (never {@code null})
	 * @throws ProjectNotFoundException if project with such {@code projectId} doesn't exist
	 */
	Project getFullById(int projectId) throws ProjectNotFoundException;
	
	/**
	 * Returns projects with name which mathes to {@code pattern}
	 * @param pattern - pattern of requested projects' names 
	 * (not RegExp, just name: "Projectica", "Spring", etc)
	 * @return List of appropriate projects, can be empty, but never {@code null}.
	 * Will always return empty list, if pattern is empty value or null.
	 */
	List<Project> getSimilarProjects(String pattern);
	
	/**
	 * Creates project and, if it was created successfully, returns it. Else throws exception
	 * @param name - desirable name of project
	 * @param creatorPosition - position which will be created for current user
	 * @param description - description of {@code creatorPosition}
	 * @return created project (never {@code null})
	 * @throws UserNotFoundException if current user is not registered
	 * @throws ProjectAlreadyExistsException  if project with such {@code name} already exists
	 * @throws ExternalResourceAccessException - if some error happened in messaging service
	 */
	Project createProject(String name, String creatorPosition, String description) throws UserNotFoundException, ProjectAlreadyExistsException, ExternalResourceAccessException;
	
	/**
	 * Returns value which present existence of project with such {@code projectName}
	 * @param projectName - name of requested project
	 * @return {@code true} - if project exists; else {@code false}
	 */
	boolean isExist(String projectName);

	/**
	 * Updates project data.
	 * @param projectId - id of project will be updated
	 * @param name - name will replace old one
	 * @param description - description will replace old one
	 * @throws EntityNotFoundException - id current user or necessary project doesn't exist
	 * @throws AccessDeniedException - if current user is not creator of necessary project
	 */
	void update(int projectId, String name, String description) throws EntityNotFoundException, AccessDeniedException;
	
}
