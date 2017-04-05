package com.vetalzloy.projectica.test.helper.proxy;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.vetalzloy.projectica.model.ChatMessage;
import com.vetalzloy.projectica.service.dao.ChatMessageDAO;

@Transactional
public class ChatMessageDAOProxy implements ChatMessageDAO {
	
	@Autowired
	@Qualifier("chatMessageDAOImpl")
	private ChatMessageDAO chatMessageDAO;
	
	@Override
	public void saveOrUpdate(ChatMessage message) {
		chatMessageDAO.saveOrUpdate(message);
	}

	@Override
	public List<ChatMessage> getFirstPage(int amount, int chatRoomId) {
		return chatMessageDAO.getFirstPage(amount, chatRoomId);
	}

	@Override
	public List<ChatMessage> getPageBeforeEarliest(long earliestMessageId, int amount, int chatRoomId) {
		return chatMessageDAO.getPageBeforeEarliest(earliestMessageId, amount, chatRoomId);
	}

}
