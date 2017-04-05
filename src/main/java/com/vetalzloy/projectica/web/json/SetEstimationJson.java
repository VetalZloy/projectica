package com.vetalzloy.projectica.web.json;

public class SetEstimationJson {
	
	private String username;
	
	private int estimation;

	public SetEstimationJson() {}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getEstimation() {
		return estimation;
	}

	public void setEstimation(int estimation) {
		this.estimation = estimation;
	}

	@Override
	public String toString() {
		return "SetEstimation [username=" + username + ", estimation=" + estimation + "]";
	}
	
}
