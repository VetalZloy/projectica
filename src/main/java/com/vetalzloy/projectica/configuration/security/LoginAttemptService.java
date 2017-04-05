package com.vetalzloy.projectica.configuration.security;

import java.util.HashMap;
import java.util.Map;

public class LoginAttemptService {
	
	private final int MAX_ATTEMPT = 10;
	private Map<String, Integer> attemptsMap = new HashMap<>();
	
	public void loginSucceeded(String address){
		attemptsMap.remove(address);
	}
	
	public void loginFailed(String address){
		Integer attemptsCount = attemptsMap.get(address);
		if(attemptsCount == null) attemptsCount = 0;		
		attemptsMap.put(address, ++attemptsCount);
	}
	
	public boolean isBlocked(String address){
		Integer attemptsCount = attemptsMap.get(address);
		if(attemptsCount == null) attemptsCount = 0;	
		if(attemptsCount > MAX_ATTEMPT) return true;
		return false;
	}
}
