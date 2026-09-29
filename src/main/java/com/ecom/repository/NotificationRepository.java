package com.ecom.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecom.model.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {

	List<Notification> findByUserIdOrderByCreatedAtDesc(Integer userId);

	Long countByUserIdAndIsReadFalse(Integer userId);
}
