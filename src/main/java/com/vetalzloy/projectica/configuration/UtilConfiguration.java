package com.vetalzloy.projectica.configuration;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.vetalzloy.projectica.util.MailUtil;
import com.vetalzloy.projectica.util.MailUtilImpl;

@Configuration
@EnableScheduling
@PropertySource(ignoreResourceNotFound=true,
				value = {
					"classpath:util.properties",
					"classpath:util-private.properties",
					"classpath:util-deploy.properties",
					"classpath:util-test.properties" // Maven excludes this file 
				})
public class UtilConfiguration {
	
	@Autowired
	private Environment env;
	
	@Bean
	@Qualifier("applicationAddress")
	public String applicationAddress(){
		return env.getProperty("util.applicationAddress");
	}
	
	@Bean
	@Qualifier("email")
	public String email(){
		return env.getProperty("email.username");
	}
	
	@Bean
	@Qualifier("message")
	public String message(){
		return env.getProperty("util.message");
	}
	
	@Bean
	@Qualifier("mailSender")
	public MailSender mailSender(){
		JavaMailSenderImpl sender = new JavaMailSenderImpl();
		sender.setHost(env.getProperty("email.host"));
		sender.setPort(env.getRequiredProperty("email.port", Integer.class));
		sender.setUsername(env.getProperty("email.username"));
		sender.setPassword(env.getProperty("email.password"));
		
		Properties prop = new Properties();
		prop.put("mail.transport.protocol", 
				 env.getProperty("mail.transport.protocol"));
		
		prop.put("mail.smtp.auth", 
				 env.getProperty("mail.smtp.auth"));
		
		prop.put("mail.smtp.starttls.enable", 
				 env.getProperty("mail.smtp.starttls.enable"));
		
		prop.put("mail.debug", 
				 env.getProperty("mail.debug"));
		
		sender.setJavaMailProperties(prop);
		return sender;
	}
	
	@Bean
	public MailUtil mailUtil(){
		return new MailUtilImpl();
	}
	
}
