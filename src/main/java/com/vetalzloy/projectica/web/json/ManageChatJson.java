package com.vetalzloy.projectica.web.json;

import java.util.List;

public class ManageChatJson {
	
	private List<Long> usersToAdd;
	
	private List<Long> usersToRemove;
	
	public ManageChatJson() {}

	public List<Long> getUsersToAdd() {
		return usersToAdd;
	}

	public void setUsersToAdd(List<Long> usersToAdd) {
		this.usersToAdd = usersToAdd;
	}

	public List<Long> getUsersToRemove() {
		return usersToRemove;
	}

	public void setUsersToRemove(List<Long> usersToRemove) {
		this.usersToRemove = usersToRemove;
	}
	
}
