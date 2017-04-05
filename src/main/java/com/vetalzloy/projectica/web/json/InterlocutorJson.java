package com.vetalzloy.projectica.web.json;

import java.time.LocalDateTime;

import com.vetalzloy.projectica.model.Interlocutor;
import com.vetalzloy.projectica.model.User;

public class InterlocutorJson implements Comparable<InterlocutorJson>{
	
	private static final int MAX_LENGTH = 30;
	private final User user;	
	private final String message;	
	private final String status;
	private final LocalDateTime date;
	
	public InterlocutorJson(User user, String message, String status, LocalDateTime date) {
		this.user = user;
		this.message = message;
		this.status = status;
		this.date = date;
	}

	public static InterlocutorJson create(Interlocutor interlocutor){
		User u = interlocutor.getInterlocutor();
		String m = interlocutor.getLastDialogMessage().getText();
		LocalDateTime d = interlocutor.getLastDialogMessage().getDate();
		if(m.length() > MAX_LENGTH) 
			m = m.substring(0, MAX_LENGTH);
		
		String s = "";
		if(! interlocutor.getLastDialogMessage().isRead()){
			String unreadUsername = interlocutor.getLastDialogMessage()
												.getReciever()
												.getUsername();
			
			if(u.getUsername().equals(unreadUsername))
				s = "unread-by-interlocutor";
			else 
				s = "unread-by-user";
		}
		
		return new InterlocutorJson(u, m, s, d);
	}
	
	public User getUser() {
		return user;
	}

	public String getMessage() {
		return message;
	}

	public String getStatus() {
		return status;
	}

	public LocalDateTime getDate() {
		return date;
	}

	@Override
	public int compareTo(InterlocutorJson i) {		
		if(date.isAfter(i.getDate())) return -1;		
		return 1;
	}
	
}
