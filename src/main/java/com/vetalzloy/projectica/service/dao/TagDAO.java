package com.vetalzloy.projectica.service.dao;

import java.util.List;

import com.vetalzloy.projectica.model.Tag;

/**
 * This interface provides methods fot working with tags on database level
 * @author VetalZloy
 *
 */
public interface TagDAO {
	
	/**
	 * Retrieves tag which has name matched to {@code pattern}
	 * @param pattern - all retrieved tags should have name matched to it 
	 * (not RegExp, just tag: "Java", "English", "REST", etc)
	 * @return List of retrieved tags, can be empty, but never {@code null}
	 */
	List<Tag> getSimilar(String pattern);
	
	/**
	 * Retrieves tag with name {@code tag}
	 * @param tag - name of necessary tag
	 * @return retrieved tag or {@code null}, if it doesn't exist
	 */
	Tag get(String tag);
	
	/**
	 * Saves new tag or updates, if it already exists
	 * @param tag
	 */
	void saveOrUpdate(Tag tag);
}
