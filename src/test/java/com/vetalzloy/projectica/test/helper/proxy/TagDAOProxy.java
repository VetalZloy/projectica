package com.vetalzloy.projectica.test.helper.proxy;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.vetalzloy.projectica.model.Tag;
import com.vetalzloy.projectica.service.dao.TagDAO;

@Transactional
public class TagDAOProxy implements TagDAO {

	@Autowired
	@Qualifier("tagDAOImpl")
	private TagDAO tagDAO;
	
	@Override
	public List<Tag> getSimilar(String tag) {
		return tagDAO.getSimilar(tag);
	}

	@Override
	public Tag get(String tag) {
		return tagDAO.get(tag);
	}

	@Override
	public void saveOrUpdate(Tag tag) {
		tagDAO.saveOrUpdate(tag);
	}

}
