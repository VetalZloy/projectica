package com.vetalzloy.projectica.test.configuration;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
@ComponentScan({
	"com.vetalzloy.projectica.util"
	})
public class UtilConfiguration {
	
	@Bean
	@Qualifier("applicationAddress")
	public String applicationAddress(){
		return "http://localhost:8080/Projectica";
	}
	
	@Bean
	@Qualifier("notificatorEmail")
	public String notificatorEmail(){
		return "reminder.notificator@gmail.com";
	}
	
	@Bean
	public MailSender mailSender(){
		JavaMailSenderImpl sender = new JavaMailSenderImpl();
		sender.setHost("smtp.gmail.com");
		sender.setPort(587);
		sender.setUsername(notificatorEmail());
		sender.setPassword("ope147qy");
		
		Properties prop = new Properties();
		prop.put("mail.transport.protocol", "smtp");
		prop.put("mail.smtp.auth", true);
		prop.put("mail.smtp.starttls.enable", true);
		prop.put("mail.debug", false);
		sender.setJavaMailProperties(prop);
		return sender;
	}
	
}
