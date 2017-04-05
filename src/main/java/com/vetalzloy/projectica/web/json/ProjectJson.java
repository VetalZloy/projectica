package com.vetalzloy.projectica.web.json;

import com.vetalzloy.projectica.model.Project;

public class ProjectJson {
	
	private static final int MAX_LENGTH = 200;	
	private final int id;	
	private final String name;	
	private final int vacanciesAmount;	
	private final String description;
	
	private ProjectJson(int id, String name, int vacanciesAmount, String description) {
		this.id = id;
		this.name = name;
		this.vacanciesAmount = vacanciesAmount;
		this.description = description;
	}
	
	public static ProjectJson create(Project p){
		int vacanciesAmount = (int) p.getPositions()
									 .stream().filter(pos -> pos.getUser() == null)
									 .count();
		String description;
		if(p.getDescription().length() > MAX_LENGTH)
			description = p.getDescription().substring(0, 197) + "...";
		else description = p.getDescription();
			
		return new ProjectJson(p.getId(), p.getName(), vacanciesAmount, description);
	}
	
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getVacanciesAmount() {
		return vacanciesAmount;
	}

	public String getDescription() {
		return description;
	}
}
