package com.vetalzloy.projectica.util;

/**
 * This interface supplies method for sending simple emails
 * @author VetalZloy
 *
 */
public interface MailUtil {
	
	public void sendMail(String to, String subject, String body);
	
}
