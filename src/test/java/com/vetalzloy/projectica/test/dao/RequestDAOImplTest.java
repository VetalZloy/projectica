package com.vetalzloy.projectica.test.dao;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.vetalzloy.projectica.model.Position;
import com.vetalzloy.projectica.model.Project;
import com.vetalzloy.projectica.model.Request;
import com.vetalzloy.projectica.model.User;
import com.vetalzloy.projectica.service.dao.PositionDAO;
import com.vetalzloy.projectica.service.dao.RequestDAO;
import com.vetalzloy.projectica.service.dao.UserDAO;
import com.vetalzloy.projectica.test.configuration.DAOConfiguration;
import com.vetalzloy.projectica.test.helper.DBHelper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=DAOConfiguration.class)
public class RequestDAOImplTest {
	
	@Autowired
	@Qualifier("positionDAOProxy")
	private PositionDAO positionDAO;
	
	@Autowired
	@Qualifier("userDAOProxy")
	private UserDAO userDAO;
	
	@Autowired
	@Qualifier("requestDAOProxy")
	private RequestDAO requestDAO;
	
	@Autowired
	private DBHelper helper;
		
	@Before
	public void cleaningDB(){
		helper.deleteAll();
	}
	
	@Test
	public void getByPositionIdAndUsernameTest(){
		Project p = new Project("p1");
		User u = new User("u1", "u1", "u1");
		Position pos = new Position("pos1", "pos1", p);
		
		userDAO.saveOrUpdate(u);
		positionDAO.saveOrUpdate(pos);
		
		Request req = new Request(pos, u);
		requestDAO.saveOrUpdate(req);
		
		Request req2 = requestDAO.getByPositionIdAndUsername(pos.getId(), u.getUsername());
		assertTrue(req.equals(req2));
	}
	
}
