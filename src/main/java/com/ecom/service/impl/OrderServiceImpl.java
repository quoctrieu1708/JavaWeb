package com.ecom.service.impl;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ecom.model.Cart;
import com.ecom.model.OrderAddress;
import com.ecom.model.OrderRequest;
import com.ecom.model.ProductOrder;
import com.ecom.repository.CartRepository;
import com.ecom.repository.ProductOrderRepository;
import com.ecom.service.OrderService;
import com.ecom.util.CommonUtil;
import com.ecom.util.OrderStatus;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private ProductOrderRepository orderRepository;

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private com.ecom.repository.ProductRepository productRepository;

	@Autowired
	private com.ecom.service.VoucherService voucherService;

	@Autowired
	private com.ecom.service.NotificationService notificationService;

	@Autowired
	private CommonUtil commonUtil;

	@Override
	public void saveOrder(Integer userid, OrderRequest orderRequest) throws Exception {

		List<Cart> carts = cartRepository.findByUserId(userid);
		if (carts.isEmpty()) {
			return;
		}

		// Calculate total order amount for voucher check
		double cartTotal = carts.stream()
				.mapToDouble(c -> c.getProduct().getDiscountPrice() * c.getQuantity())
				.sum();

		String vCode = null;
		Double totalDiscount = 0.0;
		if (orderRequest.getVoucherCode() != null && !orderRequest.getVoucherCode().trim().isEmpty()) {
			com.ecom.model.Voucher voucher = voucherService.validateAndGetVoucher(orderRequest.getVoucherCode().trim(), cartTotal);
			if (voucher != null) {
				vCode = voucher.getCode();
				totalDiscount = voucherService.calculateDiscount(voucher, cartTotal);
				voucherService.incrementUsedCount(vCode);
			}
		}

		String sharedOrderId = UUID.randomUUID().toString();
		com.ecom.model.UserDtls user = null;

		for (int i = 0; i < carts.size(); i++) {
			Cart cart = carts.get(i);
			user = cart.getUser();

			ProductOrder order = new ProductOrder();

			order.setOrderId(sharedOrderId);
			order.setOrderDate(LocalDate.now());

			order.setProduct(cart.getProduct());
			order.setPrice(cart.getProduct().getDiscountPrice());

			order.setQuantity(cart.getQuantity());
			order.setUser(cart.getUser());

			order.setStatus(OrderStatus.IN_PROGRESS.getName());
			order.setPaymentType(orderRequest.getPaymentType());

			if (vCode != null) {
				order.setVoucherCode(vCode);
				// Phân bổ giảm giá cho item (hoặc lưu toàn bộ vào item đầu tiên)
				if (i == 0) {
					order.setDiscountAmount(totalDiscount);
				} else {
					order.setDiscountAmount(0.0);
				}
			}

			OrderAddress address = new OrderAddress();
			address.setFullName(orderRequest.getFullName());
			address.setEmail(orderRequest.getEmail());
			address.setPhone(orderRequest.getPhone());
			address.setProvince(orderRequest.getProvince());
			address.setWard(orderRequest.getWard());
			address.setDetailAddress(orderRequest.getDetailAddress());

			order.setOrderAddress(address);

			ProductOrder saveOrder = orderRepository.save(order);

			// Deduct stock
			com.ecom.model.Product product = cart.getProduct();
			product.setStock(product.getStock() - cart.getQuantity());
			productRepository.save(product);

			try {
				commonUtil.sendMailForProductOrder(saveOrder, "success");
			} catch (Exception e) {
				System.err.println("Gửi email thông báo đơn hàng thất bại: " + e.getMessage());
			}
		}
		// Clear cart
		cartRepository.deleteAll(carts);

		// Send system notification to user
		if (user != null) {
			notificationService.createNotification(
					user,
					"Đặt hàng thành công",
					"Đơn hàng #" + sharedOrderId.substring(0, 8) + " đã được đặt thành công. Chúng tôi sẽ sớm giao đến bạn!",
					"ORDER",
					"/user/user-orders"
			);
		}
	}

	@Override
	public List<ProductOrder> getOrdersByUser(Integer userId) {
		List<ProductOrder> orders = orderRepository.findByUserIdOrderByIdDesc(userId);
		return orders;
	}

	@Override
	public ProductOrder updateOrderStatus(Integer id, String status) {
		Optional<ProductOrder> findById = orderRepository.findById(id);
		if (findById.isPresent()) {
			ProductOrder productOrder = findById.get();
			productOrder.setStatus(status);
			ProductOrder updateOrder = orderRepository.save(productOrder);

			// Send notification to customer
			if (updateOrder.getUser() != null) {
				notificationService.createNotification(
						updateOrder.getUser(),
						"Cập nhật đơn hàng #" + updateOrder.getOrderId().substring(0, Math.min(8, updateOrder.getOrderId().length())),
						"Trạng thái đơn hàng của bạn đã được cập nhật thành: " + status,
						"ORDER",
						"/user/user-orders"
				);
			}

			return updateOrder;
		}
		return null;
	}

	@Override
	public List<ProductOrder> getAllOrders() {
		return orderRepository.findAll();
	}

	@Override
	public Page<ProductOrder> getAllOrdersPagination(Integer pageNo, Integer pageSize) {
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		return orderRepository.findAll(pageable);

	}

	@Override
	public ProductOrder getOrdersByOrderId(String orderId) {
		return orderRepository.findByOrderId(orderId);
	}

}
