package com.vetalzloy.projectica.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vetalzloy.projectica.model.ChatRoom;
import com.vetalzloy.projectica.model.Position;
import com.vetalzloy.projectica.model.Project;
import com.vetalzloy.projectica.model.User;
import com.vetalzloy.projectica.service.dao.ProjectDAO;
import com.vetalzloy.projectica.service.exception.AccessDeniedException;
import com.vetalzloy.projectica.service.exception.EntityNotFoundException;
import com.vetalzloy.projectica.service.exception.ExternalResourceAccessException;
import com.vetalzloy.projectica.service.exception.ProjectAlreadyExistsException;
import com.vetalzloy.projectica.service.exception.ProjectNotFoundException;
import com.vetalzloy.projectica.service.exception.UserNotFoundException;
import com.vetalzloy.projectica.util.SecurityUtil;

@Service
@Transactional
public class ProjectServiceImpl implements ProjectService {
	
	private static final Logger logger = LoggerFactory.getLogger(ProjectServiceImpl.class);
	
	@Autowired
	private ProjectDAO projectDAO;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ChatService chatService;
	
	@Override
	public List<Project> getProjectsPage(int page) {
		int amount = 20; //To change this value => to change batch size in Project.positions
		logger.debug("Retreiving page with {} projects from {} ...", amount, amount*page);
		return projectDAO.getProjectsPage(page*amount, amount);
	}

	@Override
	public Project getById(int projectId) throws ProjectNotFoundException {
		logger.debug("Retreiving project by id {} ...", projectId);
		Project project = projectDAO.getById(projectId);
		if(project == null)
			throw new ProjectNotFoundException(projectId);
		return project;
	}

	@Override
	public Project createProject(String name, String creatorPosition, String description) 
										throws UserNotFoundException, ProjectAlreadyExistsException, ExternalResourceAccessException {
		
		if(isExist(name)) 
			throw new ProjectAlreadyExistsException("Project with name '" + name + "' already exists");
		
		String currentUsername = SecurityUtil.getCurrentUsername();
		
		logger.debug("Creating project with name = {} by user with username = {}", name, currentUsername);
		
		User user = userService.getByUsername(currentUsername);
		Project project = new Project(name, description, LocalDateTime.now(), user);
		Position pos = new Position(creatorPosition, "", user, project);
		pos.setHiringDate(LocalDateTime.now());
		project.getPositions().add(pos);
		
		ChatRoom room = new ChatRoom("Main chatroom", project);
		project.getChatRooms().add(room);
		
		projectDAO.saveOrUpdate(project);
		
		chatService.addUsersToChatRoom(room, user);
		
		return project;
	}

	@Override
	public Project getFullById(int projectId) throws ProjectNotFoundException {
		logger.debug("Extracting project with id = {} ...", projectId);
		Project project = projectDAO.getFullById(projectId);
		
		if(project == null)
			throw new ProjectNotFoundException(projectId);
		
		return project;
	}

	@Override
	public List<Project> getSimilarProjects(String pattern) {
		logger.debug("Retrieving projects, which are similar to pattern '{}'; invoker username = '{}'",
				pattern, SecurityUtil.getCurrentUsername());
		if(pattern == null || "".equals(pattern))
			return new ArrayList<>();
		return projectDAO.getSimilarProjects(pattern);
	}

	@Override
	public boolean isExist(String projectName) {
		logger.debug("Checking existance of project with name '{}'; for user with username '{}'",
				projectName, SecurityUtil.getCurrentUsername());
		
		Project p = projectDAO.getByName(projectName);
		if(p == null) {
			logger.info("Project with name '{}' doesn't exist.", projectName);
			return false;
		}			
		else {
			logger.info("Project with name '{}' already exists.", projectName);
			return true;
		}
	}

	@Override
	public void update(int projectId, String name, String description)
			throws EntityNotFoundException, AccessDeniedException {

		String currentUsername = SecurityUtil.getCurrentUsername();
		
		logger.debug("Updating project with id = {}, by user with username '{}' ...",
						projectId, currentUsername);
		
		User user = userService.getByUsername(currentUsername);
		if(user == null)
			throw new UserNotFoundException("User with username '" + currentUsername + "' is not registered");
		
		Project p = projectDAO.getById(projectId);
		if(p == null) 
			throw new ProjectNotFoundException(projectId);
		
		p.setName(name);
		p.setDescription(description);
		
		projectDAO.saveOrUpdate(p);
	}	
}
