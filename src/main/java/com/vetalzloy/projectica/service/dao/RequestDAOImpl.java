package com.vetalzloy.projectica.service.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.vetalzloy.projectica.model.Request;

@Repository
public class RequestDAOImpl implements RequestDAO {
	
	private static final Logger logger = LoggerFactory.getLogger(RequestDAOImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public void saveOrUpdate(Request req) {
		sessionFactory.getCurrentSession().saveOrUpdate(req);
		logger.info("Request {} was saved or updated succesfully", req);
	}

	@Override
	public Request getByPositionIdAndUsername(long positionId, String username) {
		Session session = sessionFactory.getCurrentSession();
		Request request = (Request) session.createQuery("FROM Request "
													  + "WHERE position.id = :positionId"
													  + " AND user.username = :username")
										   .setParameter("positionId", positionId)
										   .setParameter("username", username)
										   .uniqueResult();
		
		if(request == null)
			logger.info("Request with positionId = {} and username = {} doesn't exist.",
					positionId, username);
		else
			logger.info("Request with positionId = {} and username = {} was extracted succesfully. {}",
					positionId, username, request);
		return request;
	}

}
