package com.vetalzloy.projectica.service;

import com.vetalzloy.projectica.model.Request;
import com.vetalzloy.projectica.service.exception.AccessDeniedException;
import com.vetalzloy.projectica.service.exception.EntityNotFoundException;
import com.vetalzloy.projectica.service.exception.RequestNotFoundException;

/**
 * This interface provides method for working with requests to positions.
 * @author VetalZloy
 *
 */
public interface RequestService {	
	
	/**
	 * Returns request which belong to position with {@code positionId}, 
	 * if it exists. Else throws exception
	 * and which was created by user with {@code username}
	 * @param positionId - id of position which contain necessary request
	 * @param username - username of user, who created necessary request
	 * @return appropriate request
	 * @throws RequestNotFoundException if request for position 
	 * with such {@code positionId} and created by user 
	 * with such {@code username} doesn't exist
	 */
	Request getByPositionIdAndUsername(long positionId, String username) 
			throws RequestNotFoundException;
	
	/**
	 * Creates request to position with such {@code positionId} and, 
	 * if it's created successfully, returns it. Else throws an exception
	 * @param positionId - id of position which will contain request
	 * @param usersAdditions - some information from requestor
	 * @throws EntityNotFoundException if current user is not registered 
	 * or position with such {@code positionId} doesn't exist
	 * @throws AccessDeniedException if current user have already requested 
	 * position with such {@code id}
	 */
	Request createRequest(long positionId, String usersAdditions) 
			throws EntityNotFoundException, AccessDeniedException;
	
	/**
	 * Set estimation to request which belongs to position with such {@code positionId} 
	 * and created by user with such {@code username}. MB will be deleted
	 * @param positionId - id of position which contain necessary request
	 * @param username - username of user who requested this position
	 * @param estimation - preliminary estimation of user's efficiency
	 * @throws AccessDeniedException if current user is not creator of project,
	 * which contain position, which contain request from user with such {@code username}
	 * @throws EntityNotFoundException if such request doesn't exist
	 */
	void setEstimationByPositionId(long positionId, String username, int estimation)
			throws AccessDeniedException, EntityNotFoundException;
	
}
