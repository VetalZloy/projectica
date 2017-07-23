package com.vetalzloy.projectica.test.service;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.vetalzloy.projectica.model.Position;
import com.vetalzloy.projectica.model.Project;
import com.vetalzloy.projectica.model.User;
import com.vetalzloy.projectica.service.PositionService;
import com.vetalzloy.projectica.service.ProjectService;
import com.vetalzloy.projectica.service.exception.AccessDeniedException;
import com.vetalzloy.projectica.service.exception.EntityNotFoundException;
import com.vetalzloy.projectica.service.exception.ExternalResourceAccessException;
import com.vetalzloy.projectica.service.exception.ProjectAlreadyExistsException;
import com.vetalzloy.projectica.service.exception.ProjectNotFoundException;
import com.vetalzloy.projectica.service.exception.UserNotFoundException;
import com.vetalzloy.projectica.test.configuration.ServiceConfiguration;
import com.vetalzloy.projectica.test.helper.DBHelper;
import com.vetalzloy.projectica.util.SecurityUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=ServiceConfiguration.class, loader = AnnotationConfigContextLoader.class)
public class ProjectServiceImplTest {

	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private PositionService positionService;
	
	@Autowired
	private DBHelper helper;
	
	@Before
	public void cleaningDB(){
		helper.truncate();
		User user = helper.createDefaultUser();
		Authentication auth = new UsernamePasswordAuthenticationToken(user.getUsername(), "");
		SecurityContextHolder.getContext().setAuthentication(auth);
	}
	
	@Test
	public void getByIdTest() throws UserNotFoundException, ProjectAlreadyExistsException, ProjectNotFoundException, ExternalResourceAccessException{
		
		String projectName = "Just name";
		Project p1 = projectService.createProject(projectName, "boss", "11");
		
		// Just filling db
		for(int i = 0; i < 5; i++) 
			projectService.createProject(projectName+i, "boss", "11");		
		
		Project p11 = projectService.getById(p1.getId());
		assertTrue(p1.equals(p11));
	}
	
	@Test
	public void getProjectsPageTest() throws UserNotFoundException, ProjectAlreadyExistsException, ExternalResourceAccessException {
		String projectName = "Project #";
		List<Project> createdProjects = new ArrayList<>();
		for(int i = 0; i < 30; i++) {
			Project p = projectService.createProject(projectName + i, "Java developer", "description");
			createdProjects.add(p);
		}
		
		List<Project> projectsPage = projectService.getProjectsPage(1);
		assertTrue(projectsPage.size() == 10 && createdProjects.containsAll(createdProjects));
	}
	
	@Test
	public void getFullByIdTest() throws ProjectAlreadyExistsException, EntityNotFoundException, AccessDeniedException, ExternalResourceAccessException {
		String currentUsername = SecurityUtil.getCurrentUsername();
		Project p1 = projectService.createProject("Project #1", "Java developer", "description");
		
		List<Position> positions = new ArrayList<>();
		for(int i = 0; i < 10; i++) {
			Position pos = positionService.createPosition(p1.getId(), 
														  "name "+i, 
														  "desc", 
														  null);
			positions.add(pos);
		}
			
		Project p2 = projectService.getFullById(p1.getId());
		String p2CreatorUsername = p2.getCreator().getUsername();
		assertTrue(currentUsername.equals(p2CreatorUsername) && 
				   p2.getPositions().containsAll(positions));
	}
	
	@Test
	public void getSimilarTest() throws UserNotFoundException, ProjectAlreadyExistsException, ExternalResourceAccessException {
		String namePattern = "Project #";
		List<Project> projectsWithPattern = new ArrayList<>();
		for(int i = 0; i < 5; i++) {
			Project p = projectService.createProject(namePattern + i, "Java developer", "description");
			projectsWithPattern.add(p);
		}
		
		for(int i = 0; i < 30; i++) 
			projectService.createProject("Another pattern " + i, "Java developer", "description");
		
		List<Project> similarProjects = projectService.getSimilarProjects(namePattern);
		assertTrue(projectsWithPattern.equals(similarProjects));
	}
	
	@Test(expected=ProjectAlreadyExistsException.class)
	public void createProjectTest() throws UserNotFoundException, ProjectAlreadyExistsException, ExternalResourceAccessException {
		Project p = projectService.createProject("name", "Java developer", "description");
		projectService.createProject(p.getName(), "DBA", "desc");
	}
	
	@Test
	public void isExistsTest() throws UserNotFoundException, ProjectAlreadyExistsException, ExternalResourceAccessException {
		Project p = projectService.createProject("name", "Java developer", "description");
		assertTrue(projectService.isExist(p.getName()));
	}
	
	@Test
	public void updateTest() throws ProjectAlreadyExistsException, EntityNotFoundException, AccessDeniedException, ExternalResourceAccessException {
		Project p1 = projectService.createProject("name", "Java developer", "description");
		
		String newName = "newName";
		String newDescription = "newDescription";
		projectService.update(p1.getId(), newName, newDescription);
		
		Project p2 = projectService.getById(p1.getId());
		assertTrue(p2.getName().equals(newName) && p2.getDescription().equals(newDescription));
	}
}
