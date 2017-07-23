package com.vetalzloy.projectica.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vetalzloy.projectica.service.dao.UserDAO;

@Service
@Transactional
public class ScheduledUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(ScheduledUtil.class);
	
	@Autowired
	private UserDAO userDAO;

	/**
	 * Deletes password tokens, which are expired. Works by schedule.
	 */
	@Scheduled(fixedDelay=1000 * 60 * 10) // cleaning every 10 minutes
	public void deleteExpiredPasswordTokens() {
		logger.info("Cleaning expired password tokens ...");
		userDAO.deleteExpiredPasswordTokens();		
	}
	
	/**
	 * Deletes users with expired verification tokens. Works by schedule. 
	 */
	@Scheduled(fixedDelay=1000 * 60 * 10) // cleaning every 10 minutes
	public void deleteUsersWithExpiredVerificationToken(){
		logger.info("Cleaning expired verification tokens ...");
		userDAO.deleteUsersWithExpiredVerificationToken();
	}
	
}
