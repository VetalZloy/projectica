package com.vetalzloy.projectica.test.helper.proxy;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.vetalzloy.projectica.model.Position;
import com.vetalzloy.projectica.service.dao.PositionDAO;

/**
 * This class created for providing normal work of transactions
 * @author Vetal
 *
 */
@Repository
@Transactional
public class PositionDAOProxy implements PositionDAO {

	@Autowired
	@Qualifier("positionDAOImpl")
	private PositionDAO positionDAO;
	
	@Override
	public Position getById(long positionId) {
		return positionDAO.getById(positionId);
	}

	@Override
	public List<Position> getFreePositionsPage(int start, int amount) {
		return positionDAO.getFreePositionsPage(start, amount);
	}

	@Override
	public void saveOrUpdate(Position position) {
		positionDAO.saveOrUpdate(position);
	}

	@Override
	public List<Position> getFreeSimilarPositions(String namePattern, List<String> tags) {
		return positionDAO.getFreeSimilarPositions(namePattern, tags);
	}

}
