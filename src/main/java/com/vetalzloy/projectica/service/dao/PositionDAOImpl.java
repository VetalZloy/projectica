package com.vetalzloy.projectica.service.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.type.BigIntegerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.vetalzloy.projectica.model.Position;

@Repository
public class PositionDAOImpl implements PositionDAO {
	
	private static final Logger logger = LoggerFactory.getLogger(PositionDAOImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public Position getById(long positionId) {
		Position position = (Position) sessionFactory.getCurrentSession().get(Position.class, positionId);
		logger.info("Position with positionId {} was extracted. {}", positionId, position);
		return position;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Position> getFreePositionsPage(int start, int amount) {
		Session session = sessionFactory.getCurrentSession();
		
		List<Position> list = session.createQuery("FROM Position p "
												+ "WHERE p.hiringDate IS NULL")
									 .setFirstResult(start)
									 .setMaxResults(amount)
									 .list();
		
		logger.info("Positions from {} to {} were extracted, result list size = {}", 
				start, start+amount, list.size());
		return list;
	}

	@Override
	public void saveOrUpdate(Position position) {
		sessionFactory.getCurrentSession().saveOrUpdate(position);
		logger.info("Position {} was saved or updated succesfully", position);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Position> getFreeSimilarPositions(String namePattern, List<String> tags) {
		String baseHQL = "FROM Position p WHERE p.user IS NULL AND p.name LIKE :namePattern ";
		List<Position> list;
		if(tags.size() == 0) { // will return only by namePattern
			list = sessionFactory.getCurrentSession()
								 .createQuery(baseHQL)
								 .setParameter("namePattern", "%"+namePattern+"%")
								 .list();
		} else {
			
			//This query retrieves ids of positions which have ALL of necessary tags
			List<BigInteger> ids = sessionFactory
								   .getCurrentSession()
								   .createSQLQuery("SELECT pt.position_id "
								   				 + "FROM tag t "
								   				 + "INNER JOIN positions_tags pt ON t.tag_id = pt.tag_id "
								   				 + "WHERE t.tag IN (:tags) "
								   				 + "GROUP BY pt.position_id "
								   				 + "HAVING count(*) = :tagsSize")
								   .setParameterList("tags", tags)
								   .setParameter("tagsSize", tags.size())
								   .list();
			
			logger.debug("IDs of positions with such tags extracted. Size = {}", ids.size());
			
			if(ids.size() == 0){
				list = new ArrayList<>();
			} else {
				
				//Retrieve positions with retrieved before ids
				list = sessionFactory.getCurrentSession()
								 	 .createQuery(baseHQL + "AND p.id IN (:ids)")
								 	 .setParameter("namePattern", "%"+namePattern+"%")
								 	 .setParameterList("ids", ids, BigIntegerType.INSTANCE)
								 	 .list();
			}
		}

		logger.info("Free positions with name like '{}' and definite tags were extracted; list size = {}",
						namePattern, list.size());
		return list;
	}

	@Override
	public Position getFullById(long positionId) {
		Position position = getById(positionId);
		if(position == null) return null;
		
		logger.info("Reloading corresponding project creator for position with id = {}", positionId);
		Hibernate.initialize(position.getProject().getCreator().getUsername());
		
		logger.info("Reloading corresponding tags for position with id {}", positionId);
		Hibernate.initialize(position.getTags());
		
		logger.info("Reloading corresponding requests for position with id {}", positionId);
		Hibernate.initialize(position.getRequests());
		return position;
	}
	
}
