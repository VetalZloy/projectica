package com.vetalzloy.projectica.test.service;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
import com.vetalzloy.projectica.model.Request;
import com.vetalzloy.projectica.model.User;
import com.vetalzloy.projectica.service.PositionService;
import com.vetalzloy.projectica.service.ProjectService;
import com.vetalzloy.projectica.service.RequestService;
import com.vetalzloy.projectica.service.exception.AccessDeniedException;
import com.vetalzloy.projectica.service.exception.EntityNotFoundException;
import com.vetalzloy.projectica.service.exception.ExternalResourceAccessException;
import com.vetalzloy.projectica.service.exception.ProjectAlreadyExistsException;
import com.vetalzloy.projectica.test.configuration.ServiceConfiguration;
import com.vetalzloy.projectica.test.helper.DBHelper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=ServiceConfiguration.class, loader = AnnotationConfigContextLoader.class)
public class PositionServiceImplTest {

	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private PositionService positionService;
	
	@Autowired
	private RequestService requestService;
	
	@Autowired
	private DBHelper helper;
	
	private void setCurrentUsername(String username){
		Authentication auth = new UsernamePasswordAuthenticationToken(username, "");
		SecurityContextHolder.getContext().setAuthentication(auth);
	}
	
	@Before
	public void cleaningDB(){
		helper.truncate();
		User user = helper.createDefaultUser();
		Authentication auth = new UsernamePasswordAuthenticationToken(user.getUsername(), "");
		SecurityContextHolder.getContext().setAuthentication(auth);
	}
	
	@Test
	public void getByIdTest() throws ProjectAlreadyExistsException, EntityNotFoundException, AccessDeniedException, ExternalResourceAccessException {
		Project pr = projectService.createProject("name", "DBA", "desc");
		Position pos1 = positionService.createPosition(pr.getId(), 
													  "Designer", 
													  "desc", 
													  null);
		
		//Just filling the database
		for(int i = 0; i < 30; i++) {
			positionService.createPosition(pr.getId(), 
					  					   "JS developer #" + i, 
					  					   "desc", 
					  					   null);
		}
		
		Position pos2 = positionService.getById(pos1.getId());
		assertTrue(pos1.equals(pos2));
	}
	
	@Test
	public void getFullByIdTest() throws ProjectAlreadyExistsException, EntityNotFoundException, AccessDeniedException, ExternalResourceAccessException {
		List<String> tags = Arrays.asList("Java", "Spring", "Hibernate");
		Project pr = projectService.createProject("name", "DBA", "desc");
		Position pos1 = positionService.createPosition(pr.getId(), 
													  "Designer", 
													  "desc", 
													  tags);
		
		List<Request> requests = new ArrayList<>();
		for(int i = 0; i < 5; i++) {
			User user = helper.createUser("Will "+i, "vetalzloy@gmail.com", "");
			setCurrentUsername(user.getUsername());
			Request req = requestService.createRequest(pos1.getId(), "Some text");
			requests.add(req);
		}
		
		Position pos2 = positionService.getFullById(pos1.getId());		
		List<String> pos2Tags = pos2.getTags()
									.stream()
									.map(t -> t.getTag())
									.collect(Collectors.toList());		

		assertTrue(pos2.getRequests().containsAll(requests) && pos2Tags.containsAll(tags));
	}
	
	@Test
	public void getVacanciesPageTest() throws EntityNotFoundException, AccessDeniedException, ProjectAlreadyExistsException, ExternalResourceAccessException{
		Project pr = projectService.createProject("name", "DBA", "desc");
		List<Position> positions = new ArrayList<>();
		
		for(int i = 0; i < 30; i++){
			Position pos = positionService.createPosition(pr.getId(), 
														  "Designer", 
														  "desc", 
														  null);
			positions.add(pos);
		}
		
		Position lastPosition = positions.get(positions.size()-1);
		
		User u = helper.createUser("Will", "vet@gmail.com", "1");
		setCurrentUsername(u.getUsername());
		requestService.createRequest(lastPosition.getId(), "Some text");
		
		setCurrentUsername(pr.getCreator().getUsername());
		positionService.putUser(lastPosition.getId(), u.getUsername());
		
		/*
		 * one page - 25 vacancies => 0 page - 1..25; 1 page - 26..50
		 * 30-25=5, but one position has executive => 4 vacancies
		 */
		List<Position> vacancies = positionService.getVacanciesPage(1);
		assertTrue(positions.containsAll(vacancies) && vacancies.size() == 4);
	}
	
	@Test
	public void getSimilarVacanciesTest() throws ProjectAlreadyExistsException, EntityNotFoundException, AccessDeniedException, ExternalResourceAccessException{
		Project pr = projectService.createProject("name", "DBA", "desc");		

		List<String> tags = Arrays.asList("Java", "MySQL");
		List<Position> positionsWithTags = new ArrayList<>();
		for(int i = 0; i < 5; i++) {
			Position pos = positionService.createPosition(pr.getId(), 
														  "Designer " + i, 
														  "desc", 
														  tags);
			positionsWithTags.add(pos);
		}
		
		//Just filling database
		for(int i = 0; i < 10; i++){
			positionService.createPosition(pr.getId(), 
										   "Designer", 
										   "desc", 
										   Arrays.asList("Java"));
		}
		
		List<Position> similarPositions = positionService.getSimilarVacancies("", tags);
		assertTrue(positionsWithTags.equals(similarPositions));
	}
	
	@Test
	public void putUserTest() throws ProjectAlreadyExistsException, EntityNotFoundException, AccessDeniedException, ExternalResourceAccessException{
		Project pr = projectService.createProject("name", "DBA", "desc");
		Position pos = positionService.createPosition(pr.getId(), "name", "desc", null);	
		
		User u = helper.createUser("Will", "will@gmail.com", "1111");
		setCurrentUsername(u.getUsername());
		requestService.createRequest(pos.getId(), "Some text");
		
		setCurrentUsername(pr.getCreator().getUsername());
		positionService.putUser(pos.getId(), u.getUsername());		
		
		Position pos2 = positionService.getById(pos.getId());
		assertTrue(pos2.getUser().equals(u));
	}
	
	@Test
	public void closeTest() throws ProjectAlreadyExistsException, EntityNotFoundException, AccessDeniedException, ExternalResourceAccessException{
		Project pr = projectService.createProject("name", "DBA", "desc");
		Position pos = positionService.createPosition(pr.getId(), "name", "desc", null);	
		
		User u = helper.createUser("Will", "will@gmail.com", "1111");
		setCurrentUsername(u.getUsername());
		requestService.createRequest(pos.getId(), "Some text");
		
		setCurrentUsername(pr.getCreator().getUsername());
		positionService.putUser(pos.getId(), u.getUsername());
		
		positionService.close(pos.getId(), true, "Nice");
		Position closedPosition = positionService.getById(pos.getId());
		assertTrue(pos.equals(closedPosition) && closedPosition.getFiringDate() != null);
	}
	
	@Test
	public void createPosition() throws ProjectAlreadyExistsException, EntityNotFoundException, AccessDeniedException, ExternalResourceAccessException {
		Project pr = projectService.createProject("name", "DBA", "desc");
		List<Position> positions = new ArrayList<>();
		
		for(int i = 0; i < 30; i++){
			Position pos = positionService.createPosition(pr.getId(), 
														  "Designer", 
														  "desc", 
														  null);
			positions.add(pos);
		}
		
		pr = projectService.getFullById(pr.getId());
		assertTrue(pr.getPositions().containsAll(positions));
	}
	
	@Test
	public void updateTest() throws ProjectAlreadyExistsException, EntityNotFoundException, AccessDeniedException, ExternalResourceAccessException{
		Project pr = projectService.createProject("name", "DBA", "desc");
		Position pos = positionService.createPosition(pr.getId(), "name", "desc", null);	
		String newName = "newName";
		String newDescription = "newDesc";
		positionService.update(pos.getId(), newName, newDescription);
		
		pos = positionService.getById(pos.getId());
		assertTrue(pos.getName().equals(newName) && pos.getDescription().equals(newDescription));
	}
}
