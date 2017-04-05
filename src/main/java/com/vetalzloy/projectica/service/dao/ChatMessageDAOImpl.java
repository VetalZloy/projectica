package com.vetalzloy.projectica.service.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.vetalzloy.projectica.model.ChatMessage;

@Repository
public class ChatMessageDAOImpl implements ChatMessageDAO {
	
	private static final Logger logger = LoggerFactory.getLogger(ChatMessageDAOImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public void saveOrUpdate(ChatMessage message) {
		sessionFactory.getCurrentSession().saveOrUpdate(message);
		logger.info("Message - {} was saved or updated successfully", message);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ChatMessage> getFirstPage(int amount, int chatRoomId) {
		Session session = sessionFactory.getCurrentSession();
		List<ChatMessage> list = session.createQuery("FROM ChatMessage WHERE chatRoom.id = :id "
												  + "ORDER BY date DESC")
									    .setParameter("id", chatRoomId)
									    .setMaxResults(amount)
									    .list();
		
		logger.info("First messages page in chatroom with id {} was extracted successfully, result list size = {}", 
				chatRoomId, list.size());
		
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ChatMessage> getPageBeforeEarliest(long earliestMessageId, int amount, int chatRoomId) {
		Session session = sessionFactory.getCurrentSession();
		
		List<ChatMessage> messages = session.createQuery("FROM ChatMessage "
													   + "WHERE chatRoom.id = :chatRoomId "
													   + "AND id < :earliestMessageId "
													   + "ORDER BY date DESC")
											.setParameter("chatRoomId", chatRoomId)
											.setParameter("earliestMessageId", earliestMessageId)
											.setMaxResults(amount)
											.list();
		logger.info("ChatMessages in chatroom with id {} from earliest = {} were extracted, result list size = {}", 
				chatRoomId, earliestMessageId, messages.size());
		
		return messages;
	}

}
