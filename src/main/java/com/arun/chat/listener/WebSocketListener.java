package com.arun.chat.listener;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

import com.arun.chat.application.UserCtx;
import com.arun.chat.entity.WebChatMessage;

@Component
public class WebSocketListener {

	@Autowired
	private SimpMessageSendingOperations simpMessageSendingOperations;
	private UserCtx userCtx;
	
	public WebSocketListener() {
		userCtx = UserCtx.getInstance();
	}
	
	@EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        System.out.println("Received a new web socket connection");

        // Access session attributes from headerAccessor
        if(event.getUser() != null) {
        	OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) event.getUser();
        	if(token.getPrincipal() !=null) {
        		DefaultOAuth2User user = (DefaultOAuth2User) token.getPrincipal();
        		if(user.getAttribute("login") !=null) {
        			String userName = ""+user.getAttribute("login");
        			userCtx.setUser(userName);
        		}
        	}
        }
	}
	@EventListener
    public void handleWebSocketDisConnectListener(SessionConnectedEvent event) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		String userName = headerAccessor.getSessionAttributes() != null ?(String) headerAccessor.getSessionAttributes().get("username"):null;
		 // Access session attributes from headerAccessor
        if(userName != null) {

            WebChatMessage chatMessage = new WebChatMessage();
            chatMessage.setType("Leave");
            chatMessage.setSender(userName);

            simpMessageSendingOperations.convertAndSend("/topic/public", chatMessage);
        }
    }
}
