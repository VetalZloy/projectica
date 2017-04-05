package com.vetalzloy.projectica.service.dao;

import com.vetalzloy.projectica.model.Request;

/**
 * This interface provides methods fot working with requests on database level
 * @author VetalZloy
 *
 */
public interface RequestDAO {
	
	/**
	 * Saves new request or update, if it already exists
	 * @param request - request which will be saved or updated
	 */
	void saveOrUpdate(Request request);
	
	/**
	 * Retrieves request which belongs to position with id {@code positionId} 
	 * and created by user with username {@code username}
	 * @param positionId - id of position which has necessary request
	 * @param username - username of user, which created necessary request
	 * @return retieved request or {@code null}, if it doesn't exist
	 */
	Request getByPositionIdAndUsername(long positionId, String username);
}
