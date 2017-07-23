package com.vetalzloy.projectica.model;

import java.time.LocalDateTime;

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
import javax.persistence.Table;

import com.vetalzloy.projectica.util.LocalDateTimeAttributeConverter;

/**
 * This is class represents verification_token table in database.
 * @author VetalZloy
 *
 */
@Entity
@Table(name="verification_token")
public class VerificationToken {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="verification_token_id")
	private int id;
	
	@Column(name="token")
	private String token;
	
	@Column(name="expire_date")
	@Convert(converter=LocalDateTimeAttributeConverter.class)
	private LocalDateTime expireDate;
	
	/**
	 * Actually @OneToOne
	 * @see com.vetalzloy.projectica.model.User
	 */
	@ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@JoinColumn(name="user_id")
	private User user;
	
	public VerificationToken() {}
	
	public VerificationToken(String token, LocalDateTime expireDate) {
		super();
		this.token = token;
		this.expireDate = expireDate;
	}
	
	public VerificationToken(String token, LocalDateTime expireDate, User user) {
		this.token = token;
		this.expireDate = expireDate;
		this.user = user;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
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
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((token == null) ? 0 : token.hashCode());
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
		VerificationToken other = (VerificationToken) obj;
		if (token == null) {
			if (other.token != null)
				return false;
		} else if (!token.equals(other.token))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "VerificationToken [token=" + token + ", expireDate=" + expireDate + ", user=" + user + "]";
	}	
}
