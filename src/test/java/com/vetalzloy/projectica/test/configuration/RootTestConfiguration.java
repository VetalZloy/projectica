package com.vetalzloy.projectica.test.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.vetalzloy.projectica.test.stub.MailUtilStub;
import com.vetalzloy.projectica.util.MailUtil;

@Configuration
@ComponentScan(basePackages={
		"com.vetalzloy.projectica.test.helper"
})
public class RootTestConfiguration {
	
	@Bean
	@Qualifier("message")
	public String message(){
		return "default greetings message";
	}
	
	@Bean
	@Qualifier("applicationAddress")
	public String applicationAddress(){
		return "default application address";
	}
	
	@Bean
	@Qualifier("email")
	public String email(){
		return "default email";
	}
	
	@Bean
	public BCryptPasswordEncoder encoder(){
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public MailUtil mailUtil(){
		return new MailUtilStub();
	}
	
	@Bean
	@Qualifier("mailSender")
	public MailSender mailSender(){
		return null;
	}
	
}
