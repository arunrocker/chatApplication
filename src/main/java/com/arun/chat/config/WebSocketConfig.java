package com.arun.chat.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer{
	@Value("{spring.rabbitmq.host}")
	private String hostName;
	@Value("{spring.rabbitmq.port}")
	private String port;
	@Value("{spring.rabbitmq.username}")
	private String userName;
	@Value("{spring.rabbitmq.password}")
	private String password;
	
	public void registerStompEndpoints(StompEndpointRegistry registry) {
			registry.addEndpoint("/websocketChat").withSockJS(); // Just for webSocket Connection
	}
	
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.setApplicationDestinationPrefixes("/app"); // For every websocket Request should have this prefix
		 registry.enableSimpleBroker("/topic");
		/*
		 * registry.enableStompBrokerRelay("/topic").setRelayHost(hostName).setRelayPort
		 * (Integer.parseInt(port)).setClientLogin(userName)
		 * .setClientPasscode(password);
		 */
	}
}
