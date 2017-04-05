package com.vetalzloy.projectica.test.dao;

import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.vetalzloy.projectica.model.Position;
import com.vetalzloy.projectica.model.Project;
import com.vetalzloy.projectica.model.User;
import com.vetalzloy.projectica.service.dao.PositionDAO;
import com.vetalzloy.projectica.service.dao.ProjectDAO;
import com.vetalzloy.projectica.test.configuration.DAOConfiguration;
import com.vetalzloy.projectica.test.helper.DBHelper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=DAOConfiguration.class)
public class ProjectDAOImplTest {

	//For normal transaction work
	@Autowired
	@Qualifier("projectDAOProxy")
	private ProjectDAO projectDAO;
	
	//For normal transaction work
	@Autowired
	@Qualifier("positionDAOProxy")
	private PositionDAO positionDAO;
		
	@Autowired
	private DBHelper helper;
			
	@Before
	public void cleaningDB(){
		helper.deleteAll();
	}
	
	@Test
	public void getProjectsPageTest(){
		User u = new User("User", "email", "password");
		
		List<Project> list = new ArrayList<>();
		for(int i = 0; i < 10; i++)
			list.add(new Project("project#"+(i+1), "desc", LocalDateTime.now(), u));
		
		Random r = new Random();
		for(Project p: list) 
			for(int i = 0; i < r.nextInt(10); i++)
				positionDAO.saveOrUpdate(new Position(r.nextInt(1000) + "", "description", p));
		
		
		List<Project> resultPage = projectDAO.getProjectsPage(2, 5);
		
		//Checking whether exception thrown
		resultPage.get(0).getPositions().size();
		
		assertTrue(resultPage.size() == 5);
	}
	
}
