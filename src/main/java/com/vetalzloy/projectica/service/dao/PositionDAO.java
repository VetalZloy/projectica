package com.vetalzloy.projectica.service.dao;

import java.util.List;

import com.vetalzloy.projectica.model.Position;

/**
 * This interface provides methods for working with positions on database level
 * @author VetalZloy
 *
 */
public interface PositionDAO {
	
	/**
	 * Retrieves position, which has id {@code positionId}.
	 * LAZY fields won't be loaded
	 * @param positionId - id of required position
	 * @return required position or null if it doesn't exist
	 */
	Position getById(long positionId);
	
	/**
	 * Retrieves position, which has id {@code positionId}.
	 * All LAZY fields will be loaded as well.
	 * @param positionId - id of required position
	 * @return required position or null if it doesn't exist
	 */
	Position getFullById(long positionId);
	
	/**
	 * Retrieves free positions, which have name matched to {@code namePattern} 
	 * and contain <b>ALL</b> of {@code tags}
	 * @param namePattern - pattern of name of necessary positions 
	 * (not RegExp, just name: "Java developer", "3D modeler", etc)
	 * @param tags - tags which should belong to necessary position
	 * @return list of retrieved positions, can be empty, but never {@code null}
	 */
	List<Position> getFreeSimilarPositions(String namePattern, List<String> tags);
	
	/**
	 * Retrieves free positions which have id lower than {@code start}
	 * @param start - all positions which will be retrieved should have id lower than {@code start},
	 * and {@code start} should be closest number to them
	 * @param amount - max amount of positions wil be retreived
	 * @return List of retrieved positions, can be empty, but never {@code null}
	 */
	List<Position> getFreePositionsPage(int start, int amount);
	
	/**
	 * Saves new position or updates, if it already exists
	 * @param position
	 */
	void saveOrUpdate(Position position);
}
