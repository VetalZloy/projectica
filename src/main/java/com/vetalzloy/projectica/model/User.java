package com.vetalzloy.projectica.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.vetalzloy.projectica.util.LocalDateTimeAttributeConverter;

/**
 * Entity class for table {@code user}.
 * Highlights: {@code verificationTokens} and {@code passwordTokens} are actually @OneToOne,
 * but they annotated as @OneToMany to achieve lazy loading. It affects HQL queries in 
 * {@link com.vetalzloy.projectica.service.dao.UserDAOImpl}
 * 
 * This class checks equality by usernames.
 * @author Vetal
 * @see com.vetalzloy.projectica.service.dao.UserDAOImpl
 */
@Entity
@Table(name="user")
public class User {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="user_id")
	private long userId;
	
	@Column(name="username")
	private String username;
	
	@Column(name="email")
	private String email;
	
	@Column(name="password")
	private String password;
	
	@Column(name="enabled")
	private boolean enabled = false;
	
	@Column(name="registration_date")
	@Convert(converter=LocalDateTimeAttributeConverter.class)
	private LocalDateTime registrationDate = LocalDateTime.now();
	
	@Column(name="name")
	private String name;
	
	@Column(name="surname")
	private String surname;
	
	@Column(name="cv_link")
	private String cvLink;
	
	/**
	 * Actually @OneToOne
	 */
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="user")
	private Set<VerificationToken> verificationTokens = new HashSet<>();
	
	/**
	 * Actually @OneToOne
	 */
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="user")
	private Set<PasswordToken> passwordTokens = new HashSet<>();
	
	@OneToMany(mappedBy="creator", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	private Set<Project> createdProjects = new HashSet<>();
	
	@OneToMany(mappedBy="user", cascade=CascadeType.ALL)
	private Set<Position> positions = new HashSet<>();

	@ManyToMany(cascade=CascadeType.ALL)
	@JoinTable(name="users_tags", 
				joinColumns=@JoinColumn(name="user_id"),
				inverseJoinColumns=@JoinColumn(name="tag_id"))
	private Set<Tag> tags = new HashSet<>();
	
	@OneToMany(mappedBy="user", cascade=CascadeType.ALL)
	private Set<Request> requests = new HashSet<>();
	
	public User() {}

	public User(String username, String email, String password) {
		this.username = username;
		this.email = email;
		this.password = password;
	}

	public User(long userId, String username, String email, String password) {
		this(username, email, password);
		this.userId = userId;
	}
	
	public Set<VerificationToken> getVerificationTokens() {
		return verificationTokens;
	}

	public void setVerificationTokens(Set<VerificationToken> verificationTokens) {
		this.verificationTokens = verificationTokens;
	}

	public Set<PasswordToken> getPasswordTokens() {
		return passwordTokens;
	}

	public void setPasswordTokens(Set<PasswordToken> passwordTokens) {
		this.passwordTokens = passwordTokens;
	}

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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public long getUserId() {
		return userId;
	}
	
	public void setUserId(long userId) {
		this.userId = userId;
	}

	public LocalDateTime getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(LocalDateTime registrationDate) {
		this.registrationDate = registrationDate;
	}

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

	public Set<Project> getCreatedProjects() {
		return createdProjects;
	}

	public void setCreatedProjects(Set<Project> createdProjects) {
		this.createdProjects = createdProjects;
	}
	
	public Set<Position> getPositions() {
		return positions;
	}

	public void setPositions(Set<Position> positions) {
		this.positions = positions;
	}

	public Set<Tag> getTags() {
		return tags;
	}

	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}

	public Set<Request> getRequests() {
		return requests;
	}

	public void setRequests(Set<Request> requests) {
		this.requests = requests;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", username=" + username + ", email=" + email + ", enabled=" + enabled + ", registrationDate=" + registrationDate + ", name=" + name + ", surname="
				+ surname + ", cvLink=" + cvLink + "]";
	}

	@Override
	public int hashCode() {
		return username.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}
	
}
