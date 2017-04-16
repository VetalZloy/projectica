package com.vetalzloy.projectica.web.message;

import java.util.Set;

import com.vetalzloy.projectica.web.json.UserJson;

/**
 * Wrappers list of users, which are being sent by web socket
 * @author VetalZloy
 *
 */
public class UsersMessage {
	
	private String messageType = "usersMessage";
	
	private Set<UserJson> users;

	public UsersMessage() {}
	
	public UsersMessage(Set<UserJson> users) {
		super();
		this.users = users;
	}
	
	public String getMessageType() {
		return messageType;
	}

	public Set<UserJson> getUsers() {
		return users;
	}

	public void setUsers(Set<UserJson> users) {
		this.users = users;
	}
	
}
