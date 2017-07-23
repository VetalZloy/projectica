package com.vetalzloy.projectica.test.service;

import static org.junit.Assert.assertTrue;

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
import com.vetalzloy.projectica.model.Tag;
import com.vetalzloy.projectica.model.User;
import com.vetalzloy.projectica.service.PositionService;
import com.vetalzloy.projectica.service.ProjectService;
import com.vetalzloy.projectica.service.TagService;
import com.vetalzloy.projectica.service.UserService;
import com.vetalzloy.projectica.service.exception.AccessDeniedException;
import com.vetalzloy.projectica.service.exception.EntityNotFoundException;
import com.vetalzloy.projectica.service.exception.ExternalResourceAccessException;
import com.vetalzloy.projectica.service.exception.PositionNotFoundException;
import com.vetalzloy.projectica.service.exception.ProjectAlreadyExistsException;
import com.vetalzloy.projectica.service.exception.TagNotFoundException;
import com.vetalzloy.projectica.service.exception.UserNotFoundException;
import com.vetalzloy.projectica.test.configuration.ServiceConfiguration;
import com.vetalzloy.projectica.test.helper.DBHelper;
import com.vetalzloy.projectica.util.SecurityUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=ServiceConfiguration.class, loader = AnnotationConfigContextLoader.class)
public class TagServiceImplTest {

	@Autowired
	private TagService tagService;
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private PositionService positionService;
	
	@Autowired
	private UserService userService;
	
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
	public void getByTagTest() throws TagNotFoundException{
		Tag mysql = tagService.create("MySQL");
		tagService.create("Java");
		tagService.create("PHP");
		
		Tag my2 = tagService.getByTag(mysql.getTag());
		assertTrue(mysql.equals(my2));
	}
	
	@Test
	public void getSimilarTest(){
		Tag sql = tagService.create("SQL");
		Tag noSql = tagService.create("NoSQL");
		Tag sqlServer = tagService.create("SQL Server");
		tagService.create("Java");
		
		//In H2 case sensitive comparison by default
		List<Tag> similarTags = tagService.getSimilar("SQL");
		assertTrue(similarTags.size() == 3 && 
				   similarTags.containsAll(Arrays.asList(sql, noSql, sqlServer)));
	}
	
	@Test
	public void attachToUserTest() throws UserNotFoundException{
		List<String> tags = Arrays.asList("Java", "PHP", "NO SQL");
		for(String tag: tags)
			tagService.attachToUser(tag);
		User u = userService.getFullByUsername(SecurityUtil.getCurrentUsername());
		List<String> uTags = u.getTags().stream()
										.map(t -> t.getTag())
										.collect(Collectors.toList());
		assertTrue(tags.containsAll(uTags));
	}
	
	@Test
	public void detachToUserTest() throws EntityNotFoundException{
		List<String> tags = Arrays.asList("Java", "PHP", "NO SQL");
		for(String tag: tags)
			tagService.attachToUser(tag);
		
		tagService.detachFromUser("PHP");
		User u = userService.getFullByUsername(SecurityUtil.getCurrentUsername());
		List<String> uTags = u.getTags().stream()
										.map(t -> t.getTag())
										.collect(Collectors.toList());
		assertTrue(tags.containsAll(uTags) && tags.size() > uTags.size());
	}
	
	@Test
	public void attachToPositionTest() throws UserNotFoundException, ProjectAlreadyExistsException, PositionNotFoundException, AccessDeniedException, ExternalResourceAccessException{
		Project pr = projectService.createProject("Super project", "DBA", "descr");
		Position pos = pr.getPositions().iterator().next();
		List<String> tags = Arrays.asList("Java", "PHP", "NO SQL");
		for(String tag: tags)
			tagService.attachToPosition(pos.getId(), tag);
		
		pos = positionService.getFullById(pos.getId());
		List<String> posTags = pos.getTags().stream()
											.map(t -> t.getTag())
											.collect(Collectors.toList());
		assertTrue(tags.containsAll(posTags));
	}
	
	@Test
	public void detachToPositionTest() throws ProjectAlreadyExistsException, AccessDeniedException, EntityNotFoundException, ExternalResourceAccessException{
		Project pr = projectService.createProject("Super project", "DBA", "descr");
		Position pos = pr.getPositions().iterator().next();
		List<String> tags = Arrays.asList("Java", "PHP", "NO SQL");
		for(String tag: tags)
			tagService.attachToPosition(pos.getId(), tag);
		
		tagService.detachFromPosition(pos.getId(), "PHP");		
		
		pos = positionService.getFullById(pos.getId());
		List<String> posTags = pos.getTags().stream()
											.map(t -> t.getTag())
											.collect(Collectors.toList());
		assertTrue(tags.containsAll(posTags) && tags.size() > posTags.size());
	}
}
