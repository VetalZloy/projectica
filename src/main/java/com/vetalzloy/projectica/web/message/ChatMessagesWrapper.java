package com.vetalzloy.projectica.web.message;

import java.util.Arrays;
import java.util.List;

import com.vetalzloy.projectica.model.ChatMessage;

/**
 * Wrappers chat messages, which are being sent by web socket
 * @author VetalZloy
 *
 */
public class ChatMessagesWrapper {
	
	private String messageType = "chatMessages";
	private List<ChatMessage> messages;
	
	public ChatMessagesWrapper() {}

	public ChatMessagesWrapper(List<ChatMessage> messages) {
		super();
		this.messages = messages;
	}
	
	public ChatMessagesWrapper(ChatMessage ...messages) {
		super();
		this.messages = Arrays.asList(messages);
	}

	public String getMessageType() {
		return messageType;
	}

	public List<ChatMessage> getMessages() {
		return messages;
	}

	public void setMessages(List<ChatMessage> messages) {
		this.messages = messages;
	}
	
}
