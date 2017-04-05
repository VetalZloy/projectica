package com.vetalzloy.projectica.service;

import java.util.List;

import com.vetalzloy.projectica.model.Tag;
import com.vetalzloy.projectica.service.exception.AccessDeniedException;
import com.vetalzloy.projectica.service.exception.EntityNotFoundException;
import com.vetalzloy.projectica.service.exception.PositionNotFoundException;
import com.vetalzloy.projectica.service.exception.TagNotFoundException;
import com.vetalzloy.projectica.service.exception.UserNotFoundException;

/**
 * This interface provides methods for working with tags.
 * @author VetalZloy
 *
 */
public interface TagService {
	
	/**
	 * Returns {@code Tag} instance which has such {@code tag}, if it exists. 
	 * Else throws exception. "LAZY" collections are never loaded.
	 * @param tag - requested tag
	 * @return {@code Tag} instance which has such {@code tag} (never {@code null})
	 * @throws TagNotFoundException if such {@code tag} doesn't exist
	 */
	Tag getByTag(String tag) throws TagNotFoundException;
	
	/**
	 * Returns list of tags, which are match to {@code pattern}
	 * @param pattern - pattern of requested tags (not RegExp, just tag: "Java", "Spring MVC", etc)
	 * @return List of appropriate tags, can be empty, but never {@code null}
	 */
	List<Tag> getSimilar(String pattern);
	
	/**
	 * Created tag and, if it's created successfully, returns it.
	 * @param tag - desirable name of tag
	 * @return created tag
	 */
	Tag create(String tag);
	
	/**
	 * Attaches tag to current user.
	 * @param tag - name of tag which will be attached to current user
	 * @throws UserNotFoundException if current user is not registered
	 */
	void attachToUser(String tag) throws UserNotFoundException;
	
	/**
	 * Dettaches tag from current user
	 * @param tag - tag will be dettached from current user
	 * @throws EntityNotFoundException if current user is not registered 
	 * or such {@code tag} doesn't exist
	 */
	void detachFromUser(String tag) throws EntityNotFoundException;
	
	/**
	 * Attaches tag to position with such {@code positionId}
	 * @param positionId - id of required position
	 * @param tag - tag will be attached to position
	 * @throws PositionNotFoundException if position with such {@code positionId} doesn't exist
	 * @throws AccessDeniedException if current user is not creator of project 
	 * which contains position with such {@code positoinId}
	 */
	void attachToPosition(long positionId, String tag) throws PositionNotFoundException, AccessDeniedException;
	
	/**
	 * Detaches tag from position with such {@code positionId}
	 * @param positionId - id of required position
	 * @param tag - tag will be detached from position
	 * @throws EntityNotFoundException if position or tag doesn't exists 
	 * @throws AccessDeniedException if current user is not creator of project 
	 * which contains position with such {@code positoinId}
	 */
	void detachFromPosition(long positionId, String tag) throws EntityNotFoundException, AccessDeniedException;
	
}
