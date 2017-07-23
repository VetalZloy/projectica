package com.vetalzloy.projectica.web.json;

import com.vetalzloy.projectica.model.ChatRoom;

public class ChatRoomJson {
	
	private int id;
	
	private String name;
	
	private int projectId;
	
	private String projectName;
	
	private ChatRoomJson(int id, String name, int projectId, String projectName) {
		this.id = id;
		this.name = name;
		this.projectId = projectId;
		this.projectName = projectName;
	}

	public static ChatRoomJson create(ChatRoom room) {
		return new ChatRoomJson(room.getId(), 
								room.getName(), 
								room.getProject().getId(), 
								room.getProject().getName());
	}
	
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getProjectId() {
		return projectId;
	}

	public String getProjectName() {
		return projectName;
	}
}
