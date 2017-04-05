package com.vetalzloy.projectica.web.websocket;

import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

import org.springframework.security.core.Authentication;
import org.springframework.web.socket.server.standard.SpringConfigurator;

/**
 * Configurator of web sockets
 * @author VetalZloy
 *
 */
public class EndpointConfigurator extends SpringConfigurator {
	
	/**
	 * Set {@code Authentication} instance, which represents current user, because in websockets
	 * {@link SecurityUtil} cannot find such {@code Authentication}
	 */
    @Override
    public void modifyHandshake(ServerEndpointConfig config, HandshakeRequest request, HandshakeResponse response) {
    	super.modifyHandshake(config, request, response);
    
    	config.getUserProperties().put("auth", (Authentication) request.getUserPrincipal());
    }

}
