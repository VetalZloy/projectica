package com.vetalzloy.projectica.test.stub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vetalzloy.projectica.util.MailUtil;

public class MailUtilStub implements MailUtil {

	private static final Logger logger = LoggerFactory.getLogger(MailUtilStub.class);
	
	@Override
	public void sendMail(String to, String subject, String body) {
		logger.debug("Email to {}, with subject '{}' and body '{}' was sended successfully", 
						to, subject, body);
	}

}
