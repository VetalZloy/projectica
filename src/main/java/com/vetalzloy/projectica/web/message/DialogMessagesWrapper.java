package com.vetalzloy.projectica.web.message;

import java.util.Arrays;
import java.util.List;

import com.vetalzloy.projectica.model.DialogMessage;

/**
 * Wrappers dialog messages, which are being sent by web socket
 * @author VetalZloy
 *
 */
public class DialogMessagesWrapper {
	
	private String messageType = "dialogMessages";
	private List<DialogMessage> messages;
	
	public DialogMessagesWrapper() {}

	public DialogMessagesWrapper(List<DialogMessage> messages) {
		super();
		this.messages = messages;
	}
	
	public DialogMessagesWrapper(DialogMessage ...messages) {
		super();
		this.messages = Arrays.asList(messages);
	}
	
	public String getMessageType() {
		return messageType;
	}

	public List<DialogMessage> getMessages() {
		return messages;
	}

	public void setMessages(List<DialogMessage> messages) {
		this.messages = messages;
	}
	
	
}
