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
public class RequestServiceImplTest {
	
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
	public void createRequestTest() throws ProjectAlreadyExistsException, EntityNotFoundException, AccessDeniedException, ExternalResourceAccessException{
		Project pr = projectService.createProject("Super project", "base", "desc");
		Position pos = positionService.createPosition(pr.getId(), "DBA", "desc", null);
		
		List<Request> requests = new ArrayList<>();
		for(int i = 0; i < 4; i++) {
			User u = helper.createUser("user #"+i, "email@aa.ru", "111");
			setCurrentUsername(u.getUsername());
			Request req = requestService.createRequest(pos.getId(), "Some text");
			requests.add(req);
		}
		
		pos = positionService.getFullById(pos.getId());
		assertTrue(pos.getRequests().containsAll(requests));
	}
	
	@Test
	public void getByPositionIdAndUsernameTest() throws ProjectAlreadyExistsException, EntityNotFoundException, AccessDeniedException, ExternalResourceAccessException{
		Project pr = projectService.createProject("Super project", "base", "desc");
		Position pos = positionService.createPosition(pr.getId(), "DBA", "desc", null);
		
		User user1 = helper.createUser("user1", "email@aa.ru", "111");
		setCurrentUsername(user1.getUsername());
		Request req1 = requestService.createRequest(pos.getId(), "Some text");
		
		User user2 = helper.createUser("user2", "email@aa.ru", "111");
		setCurrentUsername(user2.getUsername());
		requestService.createRequest(pos.getId(), "Some text");
		
		Request req = requestService.getByPositionIdAndUsername(pos.getId(), user1.getUsername());
		assertTrue(req.equals(req1));
	}
	
}
