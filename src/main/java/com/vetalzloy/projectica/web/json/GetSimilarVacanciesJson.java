package com.vetalzloy.projectica.web.json;

import java.util.List;

public class GetSimilarVacanciesJson {
	
	private String namePattern;
	private List<String> tags;
	
	public GetSimilarVacanciesJson() {}

	public String getNamePattern() {
		return namePattern;
	}

	public void setNamePattern(String namePattern) {
		this.namePattern = namePattern;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	
}
