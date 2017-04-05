package com.vetalzloy.projectica.test.dao;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.vetalzloy.projectica.model.Tag;
import com.vetalzloy.projectica.service.dao.TagDAO;
import com.vetalzloy.projectica.test.configuration.DAOConfiguration;
import com.vetalzloy.projectica.test.helper.DBHelper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=DAOConfiguration.class)
public class TagDAOImplTest {
		
	@Autowired
	private DBHelper helper;
		
	@Autowired
	@Qualifier("tagDAOProxy")
	private TagDAO tagDAO;
	
	@Before
	public void cleaningDB(){
		helper.deleteAll();
	}
	
	@Test
	public void getSimilarTest(){
		Tag t1 = new Tag("SQL");
		Tag t2 = new Tag("MySQL");
		Tag t3 = new Tag("PostgreSQL");
		Tag t4 = new Tag("SQL Server");
		Tag t5 = new Tag("Java");
		
		tagDAO.saveOrUpdate(t1);
		tagDAO.saveOrUpdate(t2);
		tagDAO.saveOrUpdate(t3);
		tagDAO.saveOrUpdate(t4);
		tagDAO.saveOrUpdate(t5);
		
		List<Tag> list = tagDAO.getSimilar(t1.getTag());
		
		assertTrue(list.size() == 4);
		assertTrue(! list.contains(t5));
	}
	
	@Test
	public void getTest(){
		Tag t1 = new Tag("SQL");
		Tag t2 = new Tag("Java");
		
		tagDAO.saveOrUpdate(t1);
		tagDAO.saveOrUpdate(t2);
		
		Tag t11 = tagDAO.get(t1.getTag());
		assertTrue(t1.equals(t11));
		
		Tag nullTag = tagDAO.get("C#");
		assertTrue(nullTag == null);
	}
	
}
