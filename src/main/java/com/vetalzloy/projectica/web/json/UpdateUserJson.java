package com.vetalzloy.projectica.web.json;

public class UpdateUserJson {
	
	private String name;
	private String surname;
	private String cvLink;
	
	public UpdateUserJson() {}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getCvLink() {
		return cvLink;
	}

	public void setCvLink(String cvLink) {
		this.cvLink = cvLink;
	}

	@Override
	public String toString() {
		return "UpdateUserJson [name=" + name + ", surname=" + surname + ", cvLink=" + cvLink + "]";
	}
	
}
