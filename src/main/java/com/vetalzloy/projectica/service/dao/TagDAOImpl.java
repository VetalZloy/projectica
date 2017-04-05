package com.vetalzloy.projectica.service.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.vetalzloy.projectica.model.Tag;

@Repository
public class TagDAOImpl implements TagDAO {

	private static final Logger logger = LoggerFactory.getLogger(TagDAOImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Tag> getSimilar(String tag) {
		Session session = sessionFactory.getCurrentSession();
		List<Tag> list = session.createQuery("FROM Tag WHERE tag LIKE :tag")
								.setParameter("tag", "%" + tag + "%")
								.list();
		
		logger.info("Tags like '{}' were extracted, list size = {}", tag, list.size());
		return list;
	}

	@Override
	public Tag get(String tag) {
		Session session = sessionFactory.getCurrentSession();
		Tag resultTag = (Tag) session.createQuery("FROM Tag WHERE tag = :tag")
									 .setParameter("tag", tag)
									 .uniqueResult();
		
		if(resultTag == null) 
			logger.info("Tag '{}' was not found.", tag);
		else	
			logger.info("Tag '{}' was found.", tag);	
		
		return resultTag;
	}

	@Override
	public void saveOrUpdate(Tag tag) {
		sessionFactory.getCurrentSession().saveOrUpdate(tag);
		logger.info("Tag '{}' was saved.", tag.getTag());
	}

}
