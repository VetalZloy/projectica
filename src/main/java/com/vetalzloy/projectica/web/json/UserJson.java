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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserJson other = (UserJson) obj;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

}
