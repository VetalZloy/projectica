package com.vetalzloy.projectica.web.form;

public class RegistrationForm {
	
	private String username;
	private String email;
	private String password;
	private String passwordConf;

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPasswordConf() {
		return passwordConf;
	}
	public void setPasswordConf(String passwordConf) {
		this.passwordConf = passwordConf;
	}	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@Override
	public String toString() {
		return "RegistrationForm [username=" + username + ", email=" + email + ", password=" + password
				+ ", passwordConf=" + passwordConf + "]";
	}
	
	
	
}
