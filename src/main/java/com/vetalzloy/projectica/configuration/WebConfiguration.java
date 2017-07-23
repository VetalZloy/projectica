package com.vetalzloy.projectica.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

import com.vetalzloy.projectica.web.validator.RegistrationValidator;

@Configuration
public class WebConfiguration {
	
	@Bean
	public ResourceBundleMessageSource messageSource(){
		ResourceBundleMessageSource source = new ResourceBundleMessageSource();
		source.setBasename("validation");
		return source;
	}
	
	@Bean
	public RegistrationValidator registrationValidator(){
		return new RegistrationValidator();
	}
	
}
