package com.vetalzloy.projectica.test.helper.proxy;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.vetalzloy.projectica.model.Project;
import com.vetalzloy.projectica.service.dao.ProjectDAO;

/**
 * This class created for providing normal work of transactions
 * @author Vetal
 *
 */
@Repository
@Transactional
public class ProjectDAOProxy implements ProjectDAO {
		
	@Autowired
	@Qualifier("projectDAOImpl")
	private ProjectDAO projectDAO;
	
	@Override
	public List<Project> getProjectsPage(int start, int amount) {
		return projectDAO.getProjectsPage(start, amount);
	}

	@Override
	public Project getById(int projectId) {
		return projectDAO.getById(projectId);
	}

	@Override
	public void saveOrUpdate(Project project) {
		projectDAO.saveOrUpdate(project);
	}

	@Override
	public List<Project> getSimilarProjects(String pattern) {
		return projectDAO.getSimilarProjects(pattern);
	}

	@Override
	public Project getByName(String projectName) {
		return getByName(projectName);
	}

}
