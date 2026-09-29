package com.ecom.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecom.model.Notification;
import com.ecom.model.UserDtls;
import com.ecom.repository.NotificationRepository;
import com.ecom.service.NotificationService;

@Service
public class NotificationServiceImpl implements NotificationService {

	@Autowired
	private NotificationRepository notificationRepository;

	@Override
	public Notification createNotification(UserDtls user, String title, String message, String type, String targetUrl) {
		if (user == null) {
			return null;
		}
		Notification notification = new Notification();
		notification.setUser(user);
		notification.setTitle(title);
		notification.setMessage(message);
		notification.setType(type != null ? type : "SYSTEM");
		notification.setTargetUrl(targetUrl);
		notification.setIsRead(false);
		notification.setCreatedAt(LocalDateTime.now());
		return notificationRepository.save(notification);
	}

	@Override
	public List<Notification> getUserNotifications(Integer userId) {
		return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
	}

	@Override
	public Long getUnreadCount(Integer userId) {
		return notificationRepository.countByUserIdAndIsReadFalse(userId);
	}

	@Override
	public void markAsRead(Integer notificationId) {
		notificationRepository.findById(notificationId).ifPresent(n -> {
			n.setIsRead(true);
			notificationRepository.save(n);
		});
	}

	@Override
	public void markAllAsRead(Integer userId) {
		List<Notification> list = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
		for (Notification n : list) {
			if (!Boolean.TRUE.equals(n.getIsRead())) {
				n.setIsRead(true);
			}
		}
		notificationRepository.saveAll(list);
	}
}
