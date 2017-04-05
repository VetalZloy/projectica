package com.vetalzloy.projectica.test.dao;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.vetalzloy.projectica.model.Position;
import com.vetalzloy.projectica.model.Project;
import com.vetalzloy.projectica.model.Tag;
import com.vetalzloy.projectica.service.dao.PositionDAO;
import com.vetalzloy.projectica.service.dao.TagDAO;
import com.vetalzloy.projectica.test.configuration.DAOConfiguration;
import com.vetalzloy.projectica.test.helper.DBHelper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=DAOConfiguration.class)
public class PositionDAOImplTest {
	
	//For normal transaction work
	@Autowired
	@Qualifier("positionDAOProxy")
	private PositionDAO positionDAO;
	
	@Autowired
	@Qualifier("tagDAOProxy")
	private TagDAO tagDAO;
	
	@Autowired
	private DBHelper helper;
		
	@Before
	public void cleaningDB(){
		helper.deleteAll();
	}
	
	@Test
	public void getFreePositionsPageTest(){
		List<Project> projects = new ArrayList<>();
		for(int i = 0; i < 100; i++) 
			projects.add(new Project("p" + (i+1)));
		
		List<Position> positions = new ArrayList<>();
		for(int i = 0; i < projects.size(); i++)
			positions.add(new Position("pos" + (i+1), "desc", projects.get(i)));
		
		for(Position position: positions)
			positionDAO.saveOrUpdate(position);
		
		List<Position> resultPage = positionDAO.getFreePositionsPage(2, 50);
		
		assertTrue(resultPage.size() == 50);
	}
	
	
	@Test
	public void getSimiarTest() {
		Project p1 = new Project();
		List<Position> positions = Arrays.asList(new Position("Java developer", "", p1), 
												 new Position("C++ developer", "", p1), 
												 new Position("PM", "", p1), 
												 new Position("Sys. admin", "", p1));

		List<Tag> tags = Arrays.asList(new Tag("orm"), 
									   new Tag("db"), 
									   new Tag("java"), 
									   new Tag("c++"));
		
		for(Tag tag: tags) tagDAO.saveOrUpdate(tag);
		
		for(Position pos: positions){
			pos.getTags().addAll(tags);
			positionDAO.saveOrUpdate(pos);
		}
		
		List<Position> position = positionDAO.getFreeSimilarPositions("developer", Arrays.asList("orm", "java"));
		
		assertTrue(position.size() == 2);
		
		
	}
}
