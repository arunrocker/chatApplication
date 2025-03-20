package com.arun.chat.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import com.arun.chat.entity.WebChatMessage;

@Component
public class WebSocketListener {

	@Autowired
	private SimpMessageSendingOperations simpMessageSendingOperations;
	
	@EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        System.out.println("Received a new web socket connection");
    }
	@EventListener
    public void handleWebSocketDisConnectListener(SessionConnectedEvent event) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		String username = (String) headerAccessor.getSessionAttributes().get("username");
        if(username != null) {

            WebChatMessage chatMessage = new WebChatMessage();
            chatMessage.setType("Leave");
            chatMessage.setSender(username);

            simpMessageSendingOperations.convertAndSend("/topic/public", chatMessage);
        }
    }
}
