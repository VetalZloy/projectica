package com.vetalzloy.projectica.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

/**
 * This class sends mails. SO unexpectedly :)
 * @author VetalZloy
 *
 */
@Service
public class MailUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(MailUtil.class);
	
	@Autowired
	private MailSender sender;
	
	
	@Autowired
	@Qualifier("email")
	private String from;
	
	public void sendMail(String to, String subject, String body){
		logger.debug("Sending mail to {}, with subject '{}' and body '{}'", to, subject, body);
		
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(from);
		message.setTo(to);
		message.setSubject(subject);
		message.setText(body);
				
		try{
			sender.send(message);
			logger.info("Message was sended to email {}", to);
		} catch(Exception e){
			logger.warn("Error happens during mail sending", e);
			throw e;
		}		
	}	
	
}