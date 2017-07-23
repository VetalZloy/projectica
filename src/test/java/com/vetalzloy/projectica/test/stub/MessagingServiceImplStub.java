package com.vetalzloy.projectica.test.stub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.vetalzloy.projectica.model.ChatRoom;
import com.vetalzloy.projectica.model.User;
import com.vetalzloy.projectica.service.MessagingService;
import com.vetalzloy.projectica.service.exception.ExternalResourceAccessException;

@Service
public class MessagingServiceImplStub implements MessagingService {
	
	private static final Logger logger = LoggerFactory.getLogger(MessagingServiceImplStub.class);
	
	@Override
	public void addUsersToChatRoom(ChatRoom room, User... users) throws ExternalResourceAccessException {
		logger.info("Users were added");
	}

	@Override
	public void removeUsersFromChatRoom(ChatRoom room, User... users) throws ExternalResourceAccessException {
		logger.info("Users were removed");
	}

}
