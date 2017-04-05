package com.vetalzloy.projectica.web.encoder;

import java.io.StringWriter;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import org.springframework.stereotype.Component;

import com.vetalzloy.projectica.web.json.UserJson;
import com.vetalzloy.projectica.web.message.UsersMessage;

/**
 * Encodes UsersMessage to JSON string
 * @author VetalZloy
 *
 */
@Component
public class UsersMessageEncoder implements Encoder.Text<UsersMessage>{

	@Override
	public void init(EndpointConfig config) {}

	@Override
	public void destroy() {}

	@Override
	public String encode(UsersMessage message) throws EncodeException {		
		JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
		
		for(UserJson u: message.getUsers()) {
			JsonObjectBuilder object= Json.createObjectBuilder()
					                	  .add("username", u.getUsername())
					                	  .add("gravatarUrl", u.getGravatarUrl());
			arrayBuilder.add(object);
		}
		
		JsonObject jsonObject = Json.createObjectBuilder()
									.add("messageType", message.getMessageType())
									.add("users", arrayBuilder)
									.build();
		StringWriter sw = new StringWriter();
		Json.createWriter(sw).write(jsonObject);
		return sw.toString();
	}

}
