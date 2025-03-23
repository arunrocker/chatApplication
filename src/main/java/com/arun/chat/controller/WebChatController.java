package com.arun.chat.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import com.arun.chat.application.UserCtx;
import com.arun.chat.entity.WebChatMessage;

@Controller
public class WebChatController {

	private UserCtx userCtx = UserCtx.getInstance();
	@MessageMapping("/sendMessage")
	@SendTo("/topic/chat")
	public WebChatMessage sendMessage(@Payload WebChatMessage webChatMessage) {
		return webChatMessage;
	}
	@MessageMapping("/newUser")
	@SendTo("/topic/chat")
	public WebChatMessage newUser(@Payload WebChatMessage webSocketChatMessage,
			SimpMessageHeaderAccessor headerAccessor) {
		headerAccessor.getSessionAttributes().put("username", userCtx.getUser(1));
		webSocketChatMessage.setSender(userCtx.getUser(1));
		userCtx.removeUser(1);
		return webSocketChatMessage;
	}
}
