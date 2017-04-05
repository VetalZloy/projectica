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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.bytecode.internal.javassist.FieldHandled;
import org.hibernate.bytecode.internal.javassist.FieldHandler;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vetalzloy.projectica.util.LocalDateTimeAttributeConverter;

/**
 * Entity class for table {@code user}.
 * Highlights:
 * <ol>
 * 	<li> class implements {@code FieldHandled}
 * 	<li> class holds {@code FieldHandler}
 * 	<li> class has {@code @LazyToOne(LazyToOneOption.NO_PROXY)} annotation 
 * 		under {@code @OneToOne} annotation ({@code passwordToken, verificationToken})
 * </ol>
 * It's necessary for fooling Hibernate, here work of bytecode instrumention is simulated,
 * ergo, Hibernate doesn't load entities marked as {@code @OneToOne}, and these entities {@code null} always.
 * In my case it's not catastrofically, but it demands some special actions for saving and deleting,
 * details are in dao implementation.
 * 
 * This class checks equality by usernames.
 * @author Vetal
 * @see com.vetalzloy.projectica.service.dao.UserDAOImpl
 */
@Entity
@Table(name="user")
public class User  implements FieldHandled{
	
	private FieldHandler fieldHandler;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="user_id")
	private long userId;
	
	@Column(name="username")
	private String username;
	
	@Column(name="email")
	private String email;
	
	@JsonIgnore
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
	
	@OneToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="user")
	@LazyToOne(LazyToOneOption.NO_PROXY)
	private VerificationToken verificationToken;
	
	@OneToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="user")
	@LazyToOne(LazyToOneOption.NO_PROXY)
	private PasswordToken passwordToken;
	
	@OneToMany(mappedBy="creator", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	private Set<Project> createdProjects = new HashSet<>();
	
	@OneToMany(mappedBy="user", cascade=CascadeType.ALL)
	private Set<Position> positions = new HashSet<>();

	@ManyToMany(cascade=CascadeType.ALL)
	@JoinTable(name="users_tags", 
				joinColumns=@JoinColumn(name="user_id"),
				inverseJoinColumns=@JoinColumn(name="tag_id"))
	private Set<Tag> tags = new HashSet<>();
	
	@OneToMany(mappedBy="subject", cascade=CascadeType.ALL)
	private Set<Interlocutor> interlocutors = new HashSet<>();
	
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
	
	public PasswordToken getPasswordToken() {
		return passwordToken;
	}

	public void setPasswordToken(PasswordToken passwordToken) {
		this.passwordToken = passwordToken;
	}

	public VerificationToken getVerificationToken() {
		return verificationToken;
	}

	public void setVerificationToken(VerificationToken verificationToken) {
		this.verificationToken = verificationToken;
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
	
	public Set<Interlocutor> getInterlocutors() {
		return interlocutors;
	}

	public void setInterlocutors(Set<Interlocutor> interlocutors) {
		this.interlocutors = interlocutors;
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

	@Override
	public void setFieldHandler(FieldHandler fieldHandler) {
		this.fieldHandler = fieldHandler;
	}

	@Override
	public FieldHandler getFieldHandler() {
		return fieldHandler;
	}
	
}
