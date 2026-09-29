package com.ecom.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecom.model.SupportMessage;
import com.ecom.repository.SupportMessageRepository;
import com.ecom.service.SupportMessageService;

@Service
public class SupportMessageServiceImpl implements SupportMessageService {

	@Autowired
	private SupportMessageRepository supportMessageRepo;

	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");

	@Override
	public SupportMessage saveMessage(String customerId, String customerName, String sender, String recipient, String role, String content) {
		SupportMessage message = new SupportMessage();
		message.setCustomerId(customerId);
		message.setCustomerName(customerName != null && !customerName.trim().isEmpty() ? customerName : customerId);
		message.setSender(sender);
		message.setRecipient(recipient);
		message.setRole(role);
		message.setContent(content);
		message.setCreatedAt(LocalDateTime.now());
		message.setIsRead("ROLE_ADMIN".equals(role));
		return supportMessageRepo.save(message);
	}

	@Override
	public List<SupportMessage> getConversation(String customerId) {
		return supportMessageRepo.findByCustomerIdOrderByCreatedAtAsc(customerId);
	}

	@Override
	public List<Map<String, Object>> getConversationsList() {
		List<Object[]> results = supportMessageRepo.getCustomerConversations();
		List<Map<String, Object>> list = new ArrayList<>();

		for (Object[] row : results) {
			Map<String, Object> map = new HashMap<>();
			String cId = (String) row[0];
			String cName = (String) row[1];
			Object timeObj = row[2];
			Number unread = (Number) row[3];

			map.put("customerId", cId);
			map.put("customerName", (cName != null && !cName.trim().isEmpty()) ? cName : cId);

			if (timeObj instanceof LocalDateTime) {
				map.put("lastMessageTime", ((LocalDateTime) timeObj).format(FORMATTER));
			} else if (timeObj != null) {
				map.put("lastMessageTime", timeObj.toString());
			} else {
				map.put("lastMessageTime", "");
			}

			map.put("unreadCount", unread != null ? unread.intValue() : 0);
			list.add(map);
		}
		return list;
	}

	@Override
	@Transactional
	public void markConversationAsRead(String customerId) {
		List<SupportMessage> unread = supportMessageRepo.findByCustomerIdAndIsReadFalse(customerId);
		if (!unread.isEmpty()) {
			for (SupportMessage m : unread) {
				m.setIsRead(true);
			}
			supportMessageRepo.saveAll(unread);
		}
	}
}
