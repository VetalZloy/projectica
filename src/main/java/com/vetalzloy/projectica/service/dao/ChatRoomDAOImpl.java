package com.vetalzloy.projectica.service.dao;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.vetalzloy.projectica.model.ChatRoom;

@Repository
public class ChatRoomDAOImpl implements ChatRoomDAO {

	private Logger logger = LoggerFactory.getLogger(ChatRoomDAOImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public ChatRoom getById(int id) {
		ChatRoom room = (ChatRoom) sessionFactory.getCurrentSession().get(ChatRoom.class, id);
		if(room == null)
			logger.warn("ChatRoom with id = {} are not found.", id);		
		else 
			logger.info("ChatRoom with id = {} are found.", id);
		
		return room;
	}

	@Override
	public void saveOrUpdate(ChatRoom room) {
		sessionFactory.getCurrentSession().saveOrUpdate(room);
		logger.info("Chatroom: {}, was saved successfully.", room);
	}	
	
}
