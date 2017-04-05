package com.vetalzloy.projectica.test.helper.proxy;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.vetalzloy.projectica.model.ChatRoom;
import com.vetalzloy.projectica.service.dao.ChatRoomDAO;

@Transactional
public class ChatRoomDAOProxy implements ChatRoomDAO {

	@Autowired
	@Qualifier("chatRoomDAOImpl")
	private ChatRoomDAO chatRoomDAO;
	
	@Override
	public ChatRoom getById(int id) {
		return chatRoomDAO.getById(id);
	}

	@Override
	public void saveOrUpdate(ChatRoom room) {
		chatRoomDAO.saveOrUpdate(room);
	}

}
