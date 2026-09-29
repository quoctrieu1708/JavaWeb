package com.ecom.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ecom.model.SupportMessage;

public interface SupportMessageRepository extends JpaRepository<SupportMessage, Integer> {

	List<SupportMessage> findByCustomerIdOrderByCreatedAtAsc(String customerId);

	@Query("SELECT m.customerId, m.customerName, MAX(m.createdAt), " +
	       "SUM(CASE WHEN m.role = 'USER' AND (m.isRead = false OR m.isRead IS NULL) THEN 1 ELSE 0 END) " +
	       "FROM SupportMessage m GROUP BY m.customerId, m.customerName ORDER BY MAX(m.createdAt) DESC")
	List<Object[]> getCustomerConversations();

	List<SupportMessage> findByCustomerIdAndIsReadFalse(String customerId);
}
