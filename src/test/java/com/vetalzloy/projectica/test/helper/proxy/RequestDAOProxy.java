package com.vetalzloy.projectica.test.helper.proxy;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.vetalzloy.projectica.model.Request;
import com.vetalzloy.projectica.service.dao.RequestDAO;

@Transactional
public class RequestDAOProxy implements RequestDAO {

	@Autowired
	@Qualifier("requestDAOImpl")
	private RequestDAO requestDAO;
	
	@Override
	public void saveOrUpdate(Request req) {
		requestDAO.saveOrUpdate(req);
	}

	@Override
	public Request getByPositionIdAndUsername(long positionId, String username) {
		return requestDAO.getByPositionIdAndUsername(positionId, username);
	}

}
