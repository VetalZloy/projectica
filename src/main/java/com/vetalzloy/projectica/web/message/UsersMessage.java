package com.vetalzloy.projectica.web.message;

import java.util.List;

import com.vetalzloy.projectica.web.json.UserJson;

/**
 * Wrappers list of users, which are being sent by web socket
 * @author VetalZloy
 *
 */
public class UsersMessage {
	
	private String messageType = "usersMessage";
	
	private List<UserJson> users;

	public UsersMessage() {}
	
	public UsersMessage(List<UserJson> users) {
		super();
		this.users = users;
	}
	
	public String getMessageType() {
		return messageType;
	}

	public List<UserJson> getUsers() {
		return users;
	}

	public void setUsers(List<UserJson> users) {
		this.users = users;
	}
	
}
