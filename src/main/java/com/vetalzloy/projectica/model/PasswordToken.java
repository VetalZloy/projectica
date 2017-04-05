package com.vetalzloy.projectica.model;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.vetalzloy.projectica.util.LocalDateTimeAttributeConverter;

/**
 * This is class represents password_token table in database
 * @author VetalZloy
 *
 */
@Entity
@Table(name="password_token")
public class PasswordToken {
	
	@Id
	@GeneratedValue(generator="gen")
	@GenericGenerator(name="gen", strategy="foreign", parameters=@Parameter(name="property", value="user"))
	@Column(name="user_id")
	private long userId;
	
	@Column(name="password_token")
	private String passwordToken;	
	
	@Column(name="expire_date")
	@Convert(converter=LocalDateTimeAttributeConverter.class)
	private LocalDateTime expireDate;
	
	@OneToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	@PrimaryKeyJoinColumn
	private User user;
	
	public PasswordToken() {}
	
	public PasswordToken(User user, String passwordToken) {
		super();
		this.user = user;
		this.passwordToken = passwordToken;
	}
	
	public LocalDateTime getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(LocalDateTime expireDate) {
		this.expireDate = expireDate;
	}
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getPasswordToken() {
		return passwordToken;
	}
	public void setPasswordToken(String passwordToken) {
		this.passwordToken = passwordToken;
	}
	

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((expireDate == null) ? 0 : expireDate.hashCode());
		result = prime * result + ((passwordToken == null) ? 0 : passwordToken.hashCode());
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
		PasswordToken other = (PasswordToken) obj;
		if (expireDate == null) {
			if (other.expireDate != null)
				return false;
		} else if (!expireDate.equals(other.expireDate))
			return false;
		if (passwordToken == null) {
			if (other.passwordToken != null)
				return false;
		} else if (!passwordToken.equals(other.passwordToken))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PasswordToken [user=" + user + ", passwordToken=" + passwordToken + ", expireDate=" + expireDate + "]";
	}
	
	
}
