package com.vetalzloy.projectica.web.json;

public class CreateChatRoomJson {
	
	private int projectId;
	
	private String name;
	
	public CreateChatRoomJson() {}

	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
