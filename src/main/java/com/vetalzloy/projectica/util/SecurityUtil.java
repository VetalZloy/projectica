package com.vetalzloy.projectica.util;

import javax.websocket.EndpointConfig;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * This class gives username of current user. 
 * It's used by numerous classes in application, especially for logging and service layer.
 * @author VetalZloy
 *
 */
@Service("securityUtil")
public class SecurityUtil {
	
	/**
	 * 
	 * @return username of current user
	 */
	public static String getCurrentUsername(){
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}
	
	/**
	 * Checks whether current user is authorized
	 * @return {@code true} if authorized, else {@code false}
	 */
	public static boolean isLoggedIn(){
		if(getCurrentUsername().equals("anonymousUser"))
			return false;
		return true;
	}
	
	/**
	 * Authenticates current user
	 * @param config - config which has necessary Authentication instance
	 */
	public static void setAuthentication(EndpointConfig config){
		SecurityContextHolder.getContext().setAuthentication((Authentication) config.getUserProperties().get("auth"));
	}
	
}
