package com.vetalzloy.projectica.configuration.security;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.vetalzloy.projectica.model.User;
import com.vetalzloy.projectica.service.UserService;
import com.vetalzloy.projectica.service.exception.UserNotFoundException;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private static Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
	
	@Autowired
	private UserService userService;	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		logger.debug("Loading user information by username {}, for security providing ...", username);
		User user = null;
		try {
			if(username.contains("@")) // for email-based login
				user = userService.getByEmail(username);
			else //for username-based login
				user = userService.getByUsername(username);
		} catch (UserNotFoundException e) {
			logger.warn("Error happened during extracting user.", e);
		}
		
		if(user == null || !user.isEnabled()) {
			logger.info("User with username {} is not exist or not enable", username);
			return new org.springframework.security.core.userdetails.User(username, "", 
					Arrays.asList(new SimpleGrantedAuthority("ROLE_ANONYMOUS")));
		}
		
		logger.info("Loggin in user with username {}", username);		
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), 
				Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
	}
	
}
