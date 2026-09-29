package com.ecom.service;

import java.util.List;

import com.ecom.model.Notification;
import com.ecom.model.UserDtls;

public interface NotificationService {

	Notification createNotification(UserDtls user, String title, String message, String type, String targetUrl);

	List<Notification> getUserNotifications(Integer userId);

	Long getUnreadCount(Integer userId);

	void markAsRead(Integer notificationId);

	void markAllAsRead(Integer userId);
}
