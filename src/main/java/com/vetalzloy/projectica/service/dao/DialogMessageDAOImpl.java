package com.vetalzloy.projectica.service.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.vetalzloy.projectica.model.DialogMessage;

@Repository
public class DialogMessageDAOImpl implements DialogMessageDAO {

	private static final Logger logger = LoggerFactory.getLogger(DialogMessageDAOImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public void saveOrUpdate(DialogMessage message) {
		sessionFactory.getCurrentSession().saveOrUpdate(message);
		logger.info("Message {} was saved or updated successfully.", message);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DialogMessage> getFirstPage(int amount, long userId1, long userId2) {
		Session session = sessionFactory.getCurrentSession();
		
		List<DialogMessage> messages = session.createQuery("FROM DialogMessage "
														 + "WHERE sender.userId IN (:id1, :id2) "
														 + "AND reciever.userId IN (:id1, :id2) "
														 + "ORDER BY date DESC")
											  .setParameter("id1", userId1)
											  .setParameter("id2", userId2)
											  .setMaxResults(amount)
											  .list();
		
		logger.info("First messages page for users with id: {} and {} - was extracted, result list size = {}", 
				userId1, userId2, messages.size());
		return messages;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DialogMessage> getPageBeforeEarliest(long earliestMessageId, int amount, long userId1,
			long userId2) {
		Session session = sessionFactory.getCurrentSession();
		
		List<DialogMessage> messages = session.createQuery("FROM DialogMessage "
														 + "WHERE sender.userId IN (:id1, :id2) "
														 + "AND reciever.userId IN (:id1, :id2) "
														 + "AND messageId < :earliestMessageId "
														 + "ORDER BY date DESC")
											  .setParameter("id1", userId1)
											  .setParameter("id2", userId2)
											  .setParameter("earliestMessageId", earliestMessageId)
											  .setMaxResults(amount)
											  .list();
		logger.info("DialogMessages from {} for users with id: {} and {} - were extracted, result list size = {}", 
				earliestMessageId, userId1, userId2, messages.size());
		return messages;
	}

}
