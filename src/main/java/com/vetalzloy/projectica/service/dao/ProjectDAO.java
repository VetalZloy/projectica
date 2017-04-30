package com.vetalzloy.projectica.service.dao;

import java.util.List;

import com.vetalzloy.projectica.model.Project;

/**
 * This interface provides methods fot working with projects on database level
 * @author VetalZloy
 *
 */
public interface ProjectDAO {
	
	/**
	 * Retrieves projects which have id lower than {@code start}
	 * @param start - all projects which will be retrieved should have id lower than {@code start},
	 * and {@code start} should be closest number to them
	 * @param amount - max amount of projects will be retrieved
	 * @return - List of retrieved projects, can be empty, but never {@code null}
	 */
	List<Project> getProjectsPage(int start, int amount);
	
	/**
	 * Retrieves project with id {@code projectId}.
	 * LAZY fields won't be loaded
	 * @param projectId - id of necessary project
	 * @return retrieved project or {@code null}, if it doesn't exist
	 */
	Project getById(int projectId);
	
	/**
	 * Retrieves project with id {@code projectId}.
	 * LAZY fields will be loaded as well.
	 * @param projectId - id of necessary project
	 * @return retrieved project or {@code null}, if it doesn't exist
	 */
	Project getFullById(int projectId);
	
	/**
	 * Saves new project or updates, if it already exists
	 * @param project - project which will be saved or updated
	 */
	void saveOrUpdate(Project project);
	
	/**
	 * Retrieves projects which have name matched to {@code pattern}
	 * @param pattern - pattern of necessary projects' names 
	 * (not RegExp, just name: "Projectica", "Hibernate", etc)
	 * @return List of retrieved projects, can be empty, but never {@code null}
	 */
	List<Project> getSimilarProjects(String pattern);
	
	/**
	 * Retrieves project which have name {@code projectName}
	 * @param projectName - name of necessary project
	 * @return retrieved project or {@code null}, if it doesn't exist
	 */
	Project getByName(String projectName);
}
