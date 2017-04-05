package com.vetalzloy.projectica.configuration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@ComponentScan({
				"com.vetalzloy.projectica.configuration.security"/*,
				"com.vetalzloy.projectica.util"*/
				})
public class SecurityConfiguration {
	
	@Bean
	public BCryptPasswordEncoder encoder(){
		return new BCryptPasswordEncoder();
	}
	
	@Bean
    public SessionRegistry getSessionRegistry() {
        return new SessionRegistryImpl();
    }
	
	@Bean
	@Autowired
	public AuthenticationProvider provider(UserDetailsService service){
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(service);
		provider.setPasswordEncoder(encoder());
		return provider;
	}
	
	/* Uncomment for bruteforce defending
	@Bean
	public ApplicationListener<AuthenticationSuccessEvent> successListner(){
		return new AuthenticationSuccessListener();
	}
	
	@Bean
	public ApplicationListener<AuthenticationFailureBadCredentialsEvent> failureListner(){
		return new AuthenticationFailureListener();
	}
	
	@Bean
	public LoginAttemptService loginAttemptService(){
		return new LoginAttemptService();
	}
	
	@Bean
	public RequestContextListener requestContextListener(){
		return new RequestContextListener();
	}
	
	@Bean
	public  ContextLoaderListener contextLoaderListener(){
		return new ContextLoaderListener ();
	}*/
	
}
