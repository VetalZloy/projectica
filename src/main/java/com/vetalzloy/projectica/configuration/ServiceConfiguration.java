package com.vetalzloy.projectica.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.vetalzloy.projectica.service.MessagingService;
import com.vetalzloy.projectica.service.MessagingServiceImpl;

@Configuration
@PropertySource(
		ignoreResourceNotFound = true,
		value = {
				 "classpath:messaging.properties",
				 "classpath:messaging-private.properties"
				})
public class ServiceConfiguration {
	
	@Bean
	public MessagingService messagingService(){
		return new MessagingServiceImpl();
	}
}
