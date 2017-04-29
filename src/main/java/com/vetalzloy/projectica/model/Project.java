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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.vetalzloy.projectica.util.LocalDateTimeAttributeConverter;

/**
 * This is class represents project table in database.
 * @author VetalZloy
 *
 */
@Entity
@BatchSize(size=50)
@Table(name="project")
public class Project {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="project_id")
	private int id;
	
	@Column(name="project_name", unique=true)
	private String name;
	
	@Column(name="description")
	private String description;
	
	@Column(name="creation_date")
	@Convert(converter=LocalDateTimeAttributeConverter.class)
	private LocalDateTime creationDate = LocalDateTime.now();
	
	@Column(name="completion_date")
	@Convert(converter=LocalDateTimeAttributeConverter.class)
	private LocalDateTime completionDate;
	
	@ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@JoinColumn(name="creator_id")
	private User creator;
	
	@OneToMany(mappedBy="project", fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	@Fetch(FetchMode.SELECT)
	@BatchSize(size=20)
	private Set<Position> positions = new HashSet<>();

	@OneToMany(mappedBy="project", cascade=CascadeType.ALL)
	private Set<ChatRoom> chatRooms = new HashSet<>();
	
	public Project() {}
	
	public Project(String name, String description, LocalDateTime creationDate, User creator) {
		super();
		this.name = name;
		this.description = description;
		this.creationDate = creationDate;
		this.creator = creator;
	}

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

	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}

	public LocalDateTime getCompletionDate() {
		return completionDate;
	}

	public void setCompletionDate(LocalDateTime completionDate) {
		this.completionDate = completionDate;
	}

	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Set<Position> getPositions() {
		return positions;
	}

	public void setPositions(Set<Position> positions) {
		this.positions = positions;
	}

	public Set<ChatRoom> getChatRooms() {
		return chatRooms;
	}

	public void setChatRooms(Set<ChatRoom> chatRooms) {
		this.chatRooms = chatRooms;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Project other = (Project) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "Project [id=" + id + ", name=" + name + ", creationDate=" + creationDate + ", completionDate="
				+ completionDate + "]";
	}
	
}
