package com.vetalzloy.projectica.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vetalzloy.projectica.model.ChatRoom;
import com.vetalzloy.projectica.model.User;
import com.vetalzloy.projectica.service.exception.ExternalResourceAccessException;
import com.vetalzloy.projectica.web.json.ManageChatJson;
import com.vetalzloy.projectica.web.json.MessagingAuthorizationJson;

@Service
public class MessagingServiceImpl implements MessagingService {

	private static final Logger logger = org.slf4j.LoggerFactory.getLogger(MessagingServiceImpl.class);
	
	private RestTemplate restTemplate = new RestTemplate();
	
	@Autowired
	private Environment env;
	
	private String messagingURL;
	
	private String messagingName;
	
	private String messagingPassword;
	
	public MessagingServiceImpl() {}
	
	@PostConstruct
	public void init(){
		messagingURL = env.getProperty("messaging.url");		
		messagingName = env.getProperty("messaging.name");		
		messagingPassword = env.getProperty("messaging.password");
	}
	
	@Override
	public void addUsersToChatRoom(ChatRoom room, User... users) throws ExternalResourceAccessException {
		logger.debug("Adding users to chatroom with id = {}...", room.getId());
		List<Long> usersToAdd = Arrays.stream(users)
									  .map(u -> u.getUserId())
									  .collect(Collectors.toList());
		ManageChatJson json = new ManageChatJson();
		json.setUsersToAdd(usersToAdd);
		manageChatRoom(room, json);
	}

	@Override
	public void removeUsersFromChatRoom(ChatRoom room, User... users) throws ExternalResourceAccessException {
		logger.debug("Removing users from chatroom with id = {}", room.getId());
		List<Long> usersToRemove = Arrays.stream(users)
				  .map(u -> u.getUserId())
				  .collect(Collectors.toList());
		ManageChatJson json = new ManageChatJson();
		json.setUsersToRemove(usersToRemove);
		manageChatRoom(room, json);
	}
	
	/**
	 * Sends prepared JSON to messaging system
	 * @param room - chatroom for which action should be performed
	 * @param json - prepared JSON
	 * @throws ExternalResourceAccessException - if credentials are invalid or jwt is expired
	 */
	private void manageChatRoom(ChatRoom room, ManageChatJson json) throws ExternalResourceAccessException {
		String token = getToken();
		
		String url = messagingURL + "/chats/" + room.getId();		

		HttpHeaders headers = new HttpHeaders();
		headers.add("x-access-token", token);
		HttpEntity<ManageChatJson> entity = new HttpEntity<>(json, headers);
		ResponseEntity<String> res = restTemplate.exchange(url, 
														   HttpMethod.PUT, 
														   entity, 
														   String.class);
		
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(res.getBody());
			
			if (root.path("success").asBoolean() != true) {
				throw new ExternalResourceAccessException(root.path("message").asText());
			}
		} catch (IOException e) {
			ExternalResourceAccessException erae = new ExternalResourceAccessException("Exception happened during handling response");
			erae.initCause(e);
			throw erae;
		}
	}
	
	/**
	 * Authenticates in messaging system and, thus, receive jwt for performing some actions (add/remove users)
	 * @return String which represents jwt
	 * @throws ExternalResourceAccessException - if credentials are wrong
	 */
	private String getToken() throws ExternalResourceAccessException {
		MessagingAuthorizationJson json = new MessagingAuthorizationJson(messagingName, messagingPassword);
		HttpEntity<MessagingAuthorizationJson> entity = new HttpEntity<>(json);
		ResponseEntity<String> res = restTemplate.exchange(messagingURL + "/auth", 
															HttpMethod.POST, 
															entity, 
															String.class);
		
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(res.getBody());
			if (root.path("success").asBoolean() == true) {
				return root.path("token").asText();
			} else {
				throw new ExternalResourceAccessException(root.path("message").asText());
			}
		} catch(IOException e) {
			ExternalResourceAccessException erae = new ExternalResourceAccessException("Exception happened during receiving token");
			erae.initCause(e);
			throw erae;
		}
	}
	
}
