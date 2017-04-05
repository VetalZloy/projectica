package com.vetalzloy.projectica.web.json;

import com.vetalzloy.projectica.model.Position;

public class PositionJson {
	
	private final long id;
	private final String name;
	private final String description;
	
	private final int projectId;
	private final String projectName;

	private PositionJson(long id, String name, String description, int projectId, String projectName) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.projectId = projectId;
		this.projectName = projectName;
	}
	
	public static PositionJson create(Position pos) {
		return new PositionJson(pos.getId(), 
								pos.getName(), 
								pos.getDescription(), 
								pos.getProject().getId(), 
								pos.getProject().getName());
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public int getProjectId() {
		return projectId;
	}

	public String getProjectName() {
		return projectName;
	}
	
}