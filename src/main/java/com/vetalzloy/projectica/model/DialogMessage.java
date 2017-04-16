package com.vetalzloy.projectica.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.vetalzloy.projectica.util.LocalDateTimeAttributeConverter;

/**
 * This is class represents dialog_message table in database
 * @author VetalZloy
 *
 */
@Entity
@Table(name="dialog_message")
public class DialogMessage implements Comparable<DialogMessage> {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="message_id")
	private long messageId;
	
	@Column(name="text")
	private String text;
	
	@Column(name="sending_date")
	@Convert(converter=LocalDateTimeAttributeConverter.class)
	private LocalDateTime date;
	
	/**
	 * Whether message was read (this functionality is not implemented now)
	 */
	@Column(name="`read`")
	private boolean read;
	
	@ManyToOne
	private User sender;
	
	@ManyToOne
	private User reciever;
	
	public DialogMessage() {}
	
	public DialogMessage(String text, LocalDateTime date) {
		super();
		this.text = text;
		this.date = date;
	}
	
	public DialogMessage(String text, LocalDateTime date, User sender, User reciever) {
		this(text, date);
		this.sender = sender;
		this.reciever = reciever;
	}

	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

	public User getReciever() {
		return reciever;
	}

	public void setReciever(User reciever) {
		this.reciever = reciever;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public long getMessageId() {
		return messageId;
	}

	public void setMessageId(long messageId) {
		this.messageId = messageId;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

	@Override
	public String toString() {
		return "DialogMessage [messageId=" + messageId + ", date=" + date + ", sender=" + sender.getUsername()
				+ ", reciever=" + reciever.getUsername() + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (messageId ^ (messageId >>> 32));
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
		DialogMessage other = (DialogMessage) obj;
		if (messageId != other.messageId)
			return false;
		return true;
	}

	@Override
	public int compareTo(DialogMessage o) {
		if(date.isAfter(o.date)) return 1;
		else return -1;
	}
	
}
