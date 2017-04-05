package com.vetalzloy.projectica.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.vetalzloy.projectica.util.LocalDateTimeAttributeConverter;

/**
 * This is class represents chat_message table in database
 * @author VetalZloy
 *
 */
@Entity
@Table(name="chat_message")
public class ChatMessage implements Comparable<ChatMessage>{
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="chat_message_id")
	private long id;
	
	@Column(name="text")
	private String text;
	
	@Column(name="sending_date")
	@Convert(converter=LocalDateTimeAttributeConverter.class)
	private LocalDateTime date;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private User sender;
	
	@ManyToOne
	@JoinColumn(name="chat_room_id")
	private ChatRoom chatRoom;

	public ChatMessage() {}
	
	public ChatMessage(String text, LocalDateTime date, User sender, ChatRoom chatRoom) {
		this.text = text;
		this.date = date;
		this.sender = sender;
		this.chatRoom = chatRoom;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

	public ChatRoom getChatRoom() {
		return chatRoom;
	}

	public void setChatRoom(ChatRoom chatRoom) {
		this.chatRoom = chatRoom;
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
		ChatMessage other = (ChatMessage) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public int compareTo(ChatMessage o) {
		if(date.isAfter(o.date)) return 1;
		else return -1;
	}

	@Override
	public String toString() {
		return "ChatMessage [id=" + id + ", text=" + text + ", date=" + date + ", sender=" + sender + "]";
	}
}
