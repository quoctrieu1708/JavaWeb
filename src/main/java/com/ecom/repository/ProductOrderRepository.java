package com.ecom.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ecom.model.ProductOrder;

public interface ProductOrderRepository extends JpaRepository<ProductOrder, Integer> {

	List<ProductOrder> findByUserId(Integer userId);

	List<ProductOrder> findByUserIdOrderByIdDesc(Integer userId);

	ProductOrder findByOrderId(String orderId);

	List<ProductOrder> findAllByOrderId(String orderId);

	Long countByStatus(String status);

	@Query("SELECT COALESCE(SUM(po.price * po.quantity - COALESCE(po.discountAmount, 0)), 0.0) " +
	       "FROM ProductOrder po WHERE po.status != 'Đã hủy' AND po.status != 'Cancelled'")
	Double getTotalRevenue();

	@Query("SELECT po.orderDate, SUM(po.price * po.quantity) " +
	       "FROM ProductOrder po WHERE po.status != 'Đã hủy' AND po.status != 'Cancelled' " +
	       "GROUP BY po.orderDate ORDER BY po.orderDate ASC")
	List<Object[]> getRevenueByDate();

	@Query("SELECT po.product.title, SUM(po.quantity) " +
	       "FROM ProductOrder po WHERE po.status != 'Đã hủy' AND po.status != 'Cancelled' " +
	       "GROUP BY po.product.id, po.product.title ORDER BY SUM(po.quantity) DESC")
	List<Object[]> getTopSellingProducts(Pageable pageable);

	@Query("SELECT po.status, COUNT(po) FROM ProductOrder po GROUP BY po.status")
	List<Object[]> getOrderStatusCounts();
}
