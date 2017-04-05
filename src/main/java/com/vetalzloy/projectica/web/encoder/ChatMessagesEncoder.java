package com.vetalzloy.projectica.web.encoder;

import java.io.StringWriter;
import java.util.Collections;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import org.springframework.stereotype.Component;

import com.vetalzloy.projectica.model.ChatMessage;
import com.vetalzloy.projectica.util.GravatarUtil;
import com.vetalzloy.projectica.web.message.ChatMessagesWrapper;

/**
 * Encodes {@link ChatMessagesWrapper} to JSON string
 * @author VetalZloy
 * 
 */
@Component
public class ChatMessagesEncoder implements Encoder.Text<ChatMessagesWrapper>{

	@Override
	public void init(EndpointConfig config) {}

	@Override
	public void destroy() {}

	@Override
	public String encode(ChatMessagesWrapper wrapper) throws EncodeException {
		JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
		List<ChatMessage> list = wrapper.getMessages();
		Collections.sort(list);
		
		for(ChatMessage message: list) {
			JsonObjectBuilder object = Json.createObjectBuilder()
										   .add("text", message.getText())
										   .add("date", message.getDate().toString())
								  	 	   .add("senderUsername", message.getSender().getUsername())
								  	 	   .add("gravatarUrl", GravatarUtil.getGravatarUrl(
					  	 			   							message.getSender().getEmail(), 40));
			arrayBuilder.add(object);
		}
		
		long earliestId = -1;
		if(list.size() > 0) earliestId = list.get(0).getId();
		
		JsonObject jsonObject = Json.createObjectBuilder()
									.add("messageType", wrapper.getMessageType())
									.add("earliestId", earliestId)
									.add("messages", arrayBuilder)
									.build();
		
		StringWriter sw = new StringWriter();
		Json.createWriter(sw).write(jsonObject);			
		return sw.toString();
	}

}
