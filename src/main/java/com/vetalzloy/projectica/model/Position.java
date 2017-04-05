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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.vetalzloy.projectica.util.LocalDateTimeAttributeConverter;

/**
 * This is class represents position table in database. 
 * Position always has one of statuses: 
 * <ol>
 *   <li>FREE (VACANCY) - if it was just created and it doesn't have executive 
 *   (user, hiringDate, firingDate fields are null)</li>
 *   <li>ACTIVE - if it has unfired executive 
 *   (user, hiringDate fields is not null, but firingDate is null)</li>
 *   <li>CLOSED - if it has fired executive 
 *   (user, hiringDate, firingDate is not null)</li>
 * </ol>
 * 
 * @author VetalZloy
 *
 */
@Entity
@Table(name="position")
public class Position {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="position_id")
	private long id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="description")
	private String description;
	
	@Column(name="hiring_date")
	@Convert(converter=LocalDateTimeAttributeConverter.class)
	private LocalDateTime hiringDate;
	
	@Column(name="firing_date")
	@Convert(converter=LocalDateTimeAttributeConverter.class)
	private LocalDateTime firingDate;
	
	@Column(name="comment")
	private String comment;
	
	@Column(name="estimation")
	private Boolean estimation;
	
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="user_id")
	private User user;
	
	@ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinColumn(name="project_id")
	private Project project;
	
	@ManyToMany(cascade=CascadeType.ALL)
	@JoinTable(name="positions_tags", joinColumns=@JoinColumn(name="position_id"), 
									  inverseJoinColumns=@JoinColumn(name="tag_id"))
	private Set<Tag> tags = new HashSet<>();
	
	@OneToMany(mappedBy="position", cascade=CascadeType.ALL)
	private Set<Request> requests = new HashSet<>();
	
	public Position() {}	
	
	public Position(String name, String description, Project project) {
		super();
		this.name = name;
		this.description = description;
		this.project = project;
	}
	
	public Position(String name, String description, User user, Project project) {
		super();
		this.name = name;
		this.description = description;
		this.user = user;
		this.project = project;
	}

	public long getId() {
		return id;
	}

	public void setPosition(long positionId) {
		this.id = positionId;
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

	public LocalDateTime getHiringDate() {
		return hiringDate;
	}

	public void setHiringDate(LocalDateTime hiringDate) {
		this.hiringDate = hiringDate;
	}

	public LocalDateTime getFiringDate() {
		return firingDate;
	}

	public void setFiringDate(LocalDateTime firingDate) {
		this.firingDate = firingDate;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Boolean getEstimation() {
		return estimation;
	}

	public void setEstimation(Boolean estimation) {
		this.estimation = estimation;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Set<Tag> getTags() {
		return tags;
	}

	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}

	public void setId(long positionId) {
		this.id = positionId;
	}
	
	public Set<Request> getRequests() {
		return requests;
	}

	public void setRequests(Set<Request> requests) {
		this.requests = requests;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Position other = (Position) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Position [positionId=" + id + ", name=" + name + ", hiringDate=" + hiringDate + ", firingDate="
				+ firingDate + ", estimation=" + estimation + "]";
	}
}
