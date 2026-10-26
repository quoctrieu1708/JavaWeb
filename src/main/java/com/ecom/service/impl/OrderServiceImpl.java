package com.ecom.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.ecom.exception.InsufficientStockException;
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

	/**
	 * Đặt hàng trong MỘT giao dịch duy nhất: trừ kho, tăng lượt dùng voucher, tạo đơn và xóa giỏ.
	 * Nếu bất kỳ sản phẩm nào không đủ tồn kho -> ném InsufficientStockException và rollback toàn bộ.
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveOrder(Integer userid, OrderRequest orderRequest) throws Exception {

		List<Cart> carts = cartRepository.findByUserId(userid);
		if (carts.isEmpty()) {
			return;
		}

		// 1. Trừ kho nguyên tử cho từng sản phẩm (UPDATE ... WHERE stock >= qty)
		for (Cart cart : carts) {
			int qty = cart.getQuantity() != null ? cart.getQuantity() : 0;
			if (qty <= 0) {
				throw new InsufficientStockException("Số lượng sản phẩm \"" + cart.getProduct().getTitle() + "\" không hợp lệ.");
			}
			int updated = productRepository.decreaseStock(cart.getProduct().getId(), qty);
			if (updated == 0) {
				throw new InsufficientStockException("Sản phẩm \"" + cart.getProduct().getTitle()
						+ "\" không đủ hàng (còn " + Math.max(0, cart.getProduct().getStock())
						+ " sản phẩm). Vui lòng cập nhật lại số lượng trong giỏ hàng.");
			}
		}

		// 2. Tính tổng tiền & áp dụng voucher
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

		// 3. Tạo các dòng đơn hàng dùng chung một mã đơn
		String sharedOrderId = UUID.randomUUID().toString();
		com.ecom.model.UserDtls user = null;
		List<ProductOrder> savedOrders = new ArrayList<>();

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
				// Lưu toàn bộ giảm giá vào dòng đầu tiên của đơn hàng
				order.setDiscountAmount(i == 0 ? totalDiscount : 0.0);
			}

			OrderAddress address = new OrderAddress();
			address.setFullName(orderRequest.getFullName());
			address.setEmail(orderRequest.getEmail());
			address.setPhone(orderRequest.getPhone());
			address.setProvince(orderRequest.getProvince());
			address.setWard(orderRequest.getWard());
			address.setDetailAddress(orderRequest.getDetailAddress());

			order.setOrderAddress(address);

			savedOrders.add(orderRepository.save(order));
		}

		// 4. Xóa giỏ hàng
		cartRepository.deleteAll(carts);

		// 5. Thông báo trong hệ thống
		if (user != null) {
			notificationService.createNotification(
					user,
					"Đặt hàng thành công",
					"Đơn hàng #" + sharedOrderId.substring(0, 8) + " đã được đặt thành công. Chúng tôi sẽ sớm giao đến bạn!",
					"ORDER",
					"/user/user-orders"
			);
		}

		// 6. Gửi MỘT email xác nhận (bất đồng bộ) chỉ sau khi giao dịch commit thành công
		runAfterCommit(() -> commonUtil.sendOrderConfirmationMail(savedOrders, OrderStatus.IN_PROGRESS.getName()));
	}

	@Override
	public List<ProductOrder> getOrdersByUser(Integer userId) {
		List<ProductOrder> orders = orderRepository.findByUserIdOrderByIdDesc(userId);
		return orders;
	}

	/**
	 * Cập nhật trạng thái đơn hàng. Tự động hoàn kho khi chuyển sang "Đã hủy"
	 * và trừ kho lại nếu khôi phục một đơn đã hủy.
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public ProductOrder updateOrderStatus(Integer id, String status) {
		if (status == null) {
			return null;
		}
		Optional<ProductOrder> findById = orderRepository.findById(id);
		if (findById.isPresent()) {
			ProductOrder productOrder = findById.get();
			String oldStatus = productOrder.getStatus();
			String cancelName = OrderStatus.CANCEL.getName();

			if (productOrder.getProduct() != null && productOrder.getQuantity() != null) {
				Integer productId = productOrder.getProduct().getId();
				int qty = productOrder.getQuantity();
				if (cancelName.equals(status) && !cancelName.equals(oldStatus)) {
					// Hủy đơn -> hoàn trả tồn kho
					productRepository.increaseStock(productId, qty);
				} else if (cancelName.equals(oldStatus) && !cancelName.equals(status)) {
					// Khôi phục đơn đã hủy -> trừ kho lại (nếu còn đủ)
					if (productRepository.decreaseStock(productId, qty) == 0) {
						throw new InsufficientStockException("Không thể khôi phục đơn hàng: sản phẩm \""
								+ productOrder.getProduct().getTitle() + "\" không đủ tồn kho.");
					}
				}
			}

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

			// Email thông báo (bất đồng bộ) sau khi commit
			runAfterCommit(() -> commonUtil.sendMailForProductOrder(updateOrder, status));

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
		List<ProductOrder> items = orderRepository.findAllByOrderId(orderId);
		return items.isEmpty() ? null : items.get(0);
	}

	@Override
	public ProductOrder getOrderItemById(Integer id) {
		return orderRepository.findById(id).orElse(null);
	}

	@Override
	public List<ProductOrder> getOrderItemsByOrderId(String orderId) {
		return orderRepository.findAllByOrderId(orderId);
	}

	@Override
	public List<ProductOrder> getRecentOrders(int limit) {
		return orderRepository.findAll(PageRequest.of(0, limit, Sort.by("id").descending())).getContent();
	}

	@Override
	public Double getTotalRevenue() {
		return orderRepository.getTotalRevenue();
	}

	@Override
	public Long countOrders() {
		return orderRepository.count();
	}

	@Override
	public Long countByStatus(String status) {
		return orderRepository.countByStatus(status);
	}

	@Override
	public List<Object[]> getRevenueByDate() {
		return orderRepository.getRevenueByDate();
	}

	@Override
	public List<Object[]> getOrderStatusCounts() {
		return orderRepository.getOrderStatusCounts();
	}

	@Override
	public List<Object[]> getTopSellingProducts(int limit) {
		return orderRepository.getTopSellingProducts(PageRequest.of(0, limit));
	}

	/** Thực thi tác vụ sau khi giao dịch hiện tại commit; nếu không có giao dịch thì chạy ngay. */
	private void runAfterCommit(Runnable task) {
		if (TransactionSynchronizationManager.isSynchronizationActive()) {
			TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
				@Override
				public void afterCommit() {
					task.run();
				}
			});
		} else {
			task.run();
		}
	}

}
