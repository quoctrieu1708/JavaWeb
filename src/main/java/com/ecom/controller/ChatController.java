package com.ecom.controller;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ecom.model.ChatMessage;
import com.ecom.model.SupportMessage;
import com.ecom.service.SupportMessageService;

@Controller
public class ChatController {

	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	@Autowired
	private SupportMessageService supportMessageService;

	@MessageMapping("/chat.sendMessage")
	@SendTo("/topic/public")
	public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
		if (chatMessage.getTimestamp() == null) {
			chatMessage.setTimestamp(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
		}
		return chatMessage;
	}

	@MessageMapping("/chat.addUser")
	@SendTo("/topic/public")
	public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
		if (headerAccessor != null && headerAccessor.getSessionAttributes() != null) {
			headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
		}
		chatMessage.setTimestamp(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
		return chatMessage;
	}

	/**
	 * Customer sends a message to Admin
	 */
	@MessageMapping("/chat.sendToAdmin")
	public void sendToAdmin(@Payload ChatMessage chatMessage) {
		if (chatMessage.getTimestamp() == null) {
			chatMessage.setTimestamp(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
		}
		chatMessage.setRole("USER");
		chatMessage.setRecipient("ADMIN");

		// Persist to database
		supportMessageService.saveMessage(
			chatMessage.getCustomerId(),
			chatMessage.getCustomerName(),
			chatMessage.getSender(),
			"ADMIN",
			"USER",
			chatMessage.getContent()
		);

		// Send to customer's private room
		messagingTemplate.convertAndSend("/topic/chat/" + chatMessage.getCustomerId(), chatMessage);

		// Send to Admin notification channel
		messagingTemplate.convertAndSend("/topic/admin", chatMessage);
	}

	/**
	 * Admin sends a reply to a specific customer
	 */
	@MessageMapping("/chat.replyCustomer")
	public void replyCustomer(@Payload ChatMessage chatMessage) {
		if (chatMessage.getTimestamp() == null) {
			chatMessage.setTimestamp(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
		}
		chatMessage.setRole("ROLE_ADMIN");
		chatMessage.setSender("Admin CSKH");

		// Persist to database
		supportMessageService.saveMessage(
			chatMessage.getCustomerId(),
			chatMessage.getCustomerName(),
			"Admin CSKH",
			chatMessage.getCustomerId(),
			"ROLE_ADMIN",
			chatMessage.getContent()
		);

		// Send to customer's private room
		messagingTemplate.convertAndSend("/topic/chat/" + chatMessage.getCustomerId(), chatMessage);

		// Also send to admin channel
		messagingTemplate.convertAndSend("/topic/admin", chatMessage);
	}

	/**
	 * REST API: Get chat history with a specific customer
	 */
	@GetMapping("/api/chat/history")
	@ResponseBody
	public ResponseEntity<List<SupportMessage>> getHistory(@RequestParam String customerId) {
		List<SupportMessage> messages = supportMessageService.getConversation(customerId);
		return ResponseEntity.ok(messages);
	}

	/**
	 * REST API: Get list of active customer conversations for Admin
	 */
	@GetMapping("/api/chat/conversations")
	@ResponseBody
	public ResponseEntity<List<Map<String, Object>>> getConversations() {
		List<Map<String, Object>> conversations = supportMessageService.getConversationsList();
		return ResponseEntity.ok(conversations);
	}

	/**
	 * REST API: Mark conversation as read
	 */
	@PostMapping("/api/chat/mark-read")
	@ResponseBody
	public ResponseEntity<String> markRead(@RequestParam String customerId) {
		supportMessageService.markConversationAsRead(customerId);
		return ResponseEntity.ok("OK");
	}
}
