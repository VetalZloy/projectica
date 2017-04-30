package com.vetalzloy.projectica.service.dao;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.vetalzloy.projectica.model.Project;

@Repository
public class ProjectDAOImpl implements ProjectDAO {

	private static final Logger logger = LoggerFactory.getLogger(ProjectDAOImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Project> getProjectsPage(int start, int amount) {
		Session session = sessionFactory.getCurrentSession();
		List<Project> list = session.createQuery("FROM Project p")
									.setFirstResult(start)
									.setMaxResults(amount)
									.list();
		
		logger.info("Projects from {} to {} were extracted, result list size = {}", 
				start, start+amount, list.size());
		return list;
	}

	@Override
	public Project getById(int projectId) {
		Project p = (Project) sessionFactory.getCurrentSession().get(Project.class, projectId);
		
		if(p != null) 
			logger.info("Project with projectId = {} was extracted succesfully. {}", projectId, p);
		else 
			logger.warn("Project with projectId = {} doesn't exist", projectId);
		return p;
	}

	@Override
	public void saveOrUpdate(Project project) {
		sessionFactory.getCurrentSession().saveOrUpdate(project);
		logger.info("Project {} was saved or updated succesfully", project);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Project> getSimilarProjects(String pattern) {
		Session session = sessionFactory.getCurrentSession();
		List<Project> list = session.createQuery("FROM Project "
											   + "WHERE name LIKE :pattern")
								    .setParameter("pattern", "%" + pattern + "%")
								    .list();
		
		logger.info("Projects similar to '{}' were extracted, list size = {}", pattern, list.size());
		return list;
	}

	@Override
	public Project getByName(String projectName) {
		Project p = (Project) sessionFactory.getCurrentSession()
						 			        .createQuery("FROM Project "
						 			   			  	   + "WHERE name = :projectName")
						 			        .setParameter("projectName", projectName)
						 			        .uniqueResult();
		if(p != null) 
			logger.info("Project with projectName = {} was extracted succesfully. {}", projectName, p);
		else 
			logger.info("Project with projectName = {} doesn't exist", projectName);
		return p;
	}

	@Override
	public Project getFullById(int projectId) {
		Project p = getById(projectId);
		if(p == null) return null;
		
		logger.info("Reloading creator field for project with id {}", projectId);
		Hibernate.initialize(p.getCreator());
		
		logger.info("Reloading corresponding chatrooms for project with id {}", projectId);
		Hibernate.initialize(p.getChatRooms());
		
		return p;
	}

}
