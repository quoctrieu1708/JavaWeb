package com.ecom.service;

import java.util.List;
import java.util.Map;

import com.ecom.model.SupportMessage;

public interface SupportMessageService {

	SupportMessage saveMessage(String customerId, String customerName, String sender, String recipient, String role, String content);

	List<SupportMessage> getConversation(String customerId);

	List<Map<String, Object>> getConversationsList();

	void markConversationAsRead(String customerId);
}
