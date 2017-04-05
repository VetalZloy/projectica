package com.vetalzloy.projectica.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service("onlineUtil")
public class OnlineUtil {
	
	@Autowired
	private SessionRegistry registry;
	
	public boolean isOnline(String username){
		for(Object o: registry.getAllPrincipals())
			if(((User) o).getUsername().equals(username))
				return true;
		
		return false;
	}
	
}
