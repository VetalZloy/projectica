package com.vetalzloy.projectica.web.json;

import java.util.List;

public class CreatePositionJson {
	
	private int projectId;
	
	private String name;
	
	private String description;
	
	private List<String> tags;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	@Override
	public String toString() {
		return "CreatePosition [projectId=" + projectId + ", name=" + name + ", description=" + description + ", tags="
				+ tags + "]";
	}
	
}
