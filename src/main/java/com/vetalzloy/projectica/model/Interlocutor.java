package com.vetalzloy.projectica.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * This class represents `interlocutor` table from db. 
 * It has owner as {@code subject}, his interlocutor as {@code interlocutor}, 
 * and last message which was sended in dialog between {@code subject} and {@code interlocutor} 
 * (doesn't matter from whom to whom)
 * This class checks equality by subject and interlocutor fields => by their usernames
 * @author VetalZloy
 *
 */
@Entity
@Table(name="interlocutor")
public class Interlocutor {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private long id;

	@ManyToOne
	@JoinColumn(name="last_dialog_message_id")
	private DialogMessage lastDialogMessage;

	@ManyToOne
	@JoinColumn(name="subject_id")
	private User subject;

	@ManyToOne
	@JoinColumn(name="interlocutor_id")
	private User interlocutor;

	public Interlocutor() {}
	
	public Interlocutor(User subject, User interlocutor) {
		this.subject = subject;
		this.interlocutor = interlocutor;
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public DialogMessage getLastDialogMessage() {
		return this.lastDialogMessage;
	}

	public void setLastDialogMessage(DialogMessage lastDialogMessage) {
		this.lastDialogMessage = lastDialogMessage;
	}

	public User getSubject() {
		return this.subject;
	}

	public void setSubject(User subject) {
		this.subject = subject;
	}

	public User getInterlocutor() {
		return this.interlocutor;
	}

	public void setInterlocutor(User interlocutor) {
		this.interlocutor = interlocutor;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((interlocutor == null) ? 0 : interlocutor.hashCode());
		result = prime * result + ((subject == null) ? 0 : subject.hashCode());
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
		Interlocutor other = (Interlocutor) obj;
		if (interlocutor == null) {
			if (other.interlocutor != null)
				return false;
		} else if (!interlocutor.equals(other.interlocutor))
			return false;
		if (subject == null) {
			if (other.subject != null)
				return false;
		} else if (!subject.equals(other.subject))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Interlocutor [id=" + id + ", lastDialogMessage=" + lastDialogMessage + ", subject=" + subject
				+ ", interlocutor=" + interlocutor + "]";
	}
	
}