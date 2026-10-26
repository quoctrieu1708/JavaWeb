package com.ecom.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.ecom.model.OrderRequest;
import com.ecom.model.ProductOrder;

public interface OrderService {

	public void saveOrder(Integer userid, OrderRequest orderRequest) throws Exception;

	public List<ProductOrder> getOrdersByUser(Integer userId);

	public ProductOrder updateOrderStatus(Integer id, String status);

	public List<ProductOrder> getAllOrders();

	public ProductOrder getOrdersByOrderId(String orderId);
	
	public Page<ProductOrder> getAllOrdersPagination(Integer pageNo,Integer pageSize);

	public ProductOrder getOrderItemById(Integer id);

	/** Lấy tất cả dòng sản phẩm thuộc cùng một mã đơn hàng (dùng cho hóa đơn PDF). */
	public List<ProductOrder> getOrderItemsByOrderId(String orderId);

	public List<ProductOrder> getRecentOrders(int limit);

	// Thống kê cho Dashboard quản trị
	public Double getTotalRevenue();

	public Long countOrders();

	public Long countByStatus(String status);

	public List<Object[]> getRevenueByDate();

	public List<Object[]> getOrderStatusCounts();

	public List<Object[]> getTopSellingProducts(int limit);
}
