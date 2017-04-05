package com.vetalzloy.projectica.web.json;

import com.vetalzloy.projectica.model.User;
import com.vetalzloy.projectica.util.GravatarUtil;

public class UserJson {
	
	private static final int DEFAULT_GRAVATAR_SIZE = 80;
	
	private final String username;
	private final String gravatarUrl;
	
	private UserJson(String username, String gravatarUrl) {
		this.username = username;
		this.gravatarUrl = gravatarUrl;
	}
	
	public static UserJson create(User user){
		return create(user, DEFAULT_GRAVATAR_SIZE);
	}
	
	public static UserJson create(User user, int gravatarSize) {
		return new UserJson(user.getUsername(), 
							GravatarUtil.getGravatarUrl(user.getEmail(), gravatarSize));
	}
	
	public String getUsername() {
		return username;
	}

	public String getGravatarUrl() {
		return gravatarUrl;
	}
}
