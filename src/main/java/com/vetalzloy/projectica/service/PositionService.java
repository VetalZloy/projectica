package com.vetalzloy.projectica.service;

import java.util.List;

import com.vetalzloy.projectica.model.Position;
import com.vetalzloy.projectica.service.exception.AccessDeniedException;
import com.vetalzloy.projectica.service.exception.EntityNotFoundException;
import com.vetalzloy.projectica.service.exception.ExternalResourceAccessException;
import com.vetalzloy.projectica.service.exception.PositionNotFoundException;

/**
 * This interface provides methods for working with positions.
 * @author VetalZloy
 *
 */
public interface PositionService {
	
	/**
	 * Returns position with id {@code positionId} and, if it exists, 
	 * loads all "LAZY" collections. Else throws exception.
	 * @param positionId - id of requested position
	 * @return retrieved position with loaded "LAZY" collections (never {@code null})
	 * @throws PositionNotFoundException if position with such {@code positionId} doesn't exist
	 */
	Position getFullById(long positionId) throws PositionNotFoundException;
	
	/**
	 * Returns position with id {@code positionId}, if it exists. Else throws exception.
	 * Attention: "LAZY" collections won't be loaded.
	 * @param positionId id of requested position
 	 * @return retrieved position without "LAZY" collections (never {@code null})
 	 * @throws PositionNotFoundException if position with such {@code positionId} doesn't exist
 	 * @see getFullById(long positionId)
	 */
	Position getById(long positionId) throws PositionNotFoundException;
	
	/**
	 * Returns page of free positions without "LAZY" fields.
	 * Amount of retrieved projects is under control of implementation
	 * @param page - number of requested page.
	 * @return List of free positions, can be empty, but never {@code null}
	 */
	List<Position> getVacanciesPage(int page);
	
	/**
	 * Returns free positions which match to {@code namePattern} 
	 * and/or have <b>ALL</b> of {@code tags}
	 * Amount of retrieved projects is under control of implementation
	 * @param namePattern - pattern of requested positions' name
	 * (not RegExp, just name: "Java developer", "PM", etc)
	 * @param tags - tags which belongs to requested positions
	 * @return List of appropriate free positions, can be empty, but never {@code null}.
	 * Will be always empty if both of parameters is empty value or null.
	 */
	List<Position> getSimilarVacancies(String namePattern, List<String> tags);
	
	/**
	 * Puts user to position. After that position is not free(vacancy), but active.
	 * @param positionId - id of position to which user will be set
	 * @param username - username of user, who will be set to position
	 * @throws EntityNotFoundException if user with such {@code username} is not registered 
	 * or if position with such {@code positionId} or related project doesn't exist
	 * @throws AccessDeniedException if current user is not creator of project 
	 * which consists position with such {@code positionId}
	 * @throws ExternalResourceAccessException - if error occurs in messaging system
	 */
	void putUser(long positionId, String username) throws EntityNotFoundException, AccessDeniedException, ExternalResourceAccessException;
	
	/**
	 * Sets estimation and comment to active position => makes it closed.
	 * @param positionId - id of requested position
	 * @param estimation - estimation of user's work 
	 * ({@code true} - good job, {@code false} - bad job)
	 * @param comment - comment about user's job
	 * @throws EntityNotFoundException if position with such {@code positionId} or related project doesn't exist
	 * @throws AccessDeniedException if current user is not creator of project 
	 * which consists position with such {@code positionId} or if this position is already has executive
	 * @throws ExternalResourceAccessException - if error occurs in messaging system
	 */
	void close(long positionId, boolean estimation, String comment) throws EntityNotFoundException, AccessDeniedException, ExternalResourceAccessException;
	
	/**
	 * Creates new position and, if it was created successfully, returns it. Else throws an exception.
	 * @param projectId - id of project which will consist this position
	 * @param name - name of position
	 * @param description - description of position
	 * @param tags - tags of position
	 * @return created position (never {@code null})
	 * @throws EntityNotFoundException if project with such {@code projectId} doesn't exist
	 * @throws AccessDeniedException if current user is not creator of project 
	 * which consists position with such {@code positionId}
	 */
	Position createPosition(int projectId, String name, String description, List<String> tags) throws EntityNotFoundException, AccessDeniedException;
	
	/**
	 * Updates position data.
	 * @param positionId - id of position will be updated
	 * @param name - name will replace old one
	 * @param description - description will replace old one
	 * @throws EntityNotFoundException - if current user or position doesn't exist
	 * @throws AccessDeniedException - if current user is not creator of project, 
	 * which contain such position
	 */
	void update(long positionId, String name, String description) throws EntityNotFoundException, AccessDeniedException;
	
}
