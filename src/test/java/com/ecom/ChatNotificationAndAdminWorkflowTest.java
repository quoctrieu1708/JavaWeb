package com.ecom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.ecom.model.Notification;
import com.ecom.model.SupportMessage;
import com.ecom.model.UserDtls;
import com.ecom.service.NotificationService;
import com.ecom.service.OrderService;
import com.ecom.service.ProductService;
import com.ecom.service.SupportMessageService;
import com.ecom.service.UserService;

/**
 * WORKFLOW TEST NHÓM 5: Trung tâm Thông báo, Hỗ trợ trực tuyến Live Chat 1-on-1 & Thống kê Quản trị Admin.
 * Thay thế thao tác thủ công cho các kịch bản PRF-04, CHAT-01 -> CHAT-03 và ADM-01.
 */
@SpringBootTest
@Transactional
class ChatNotificationAndAdminWorkflowTest {

	@Autowired
	private NotificationService notificationService;

	@Autowired
	private SupportMessageService supportMessageService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private ProductService productService;

	@Autowired
	private UserService userService;

	@Test
	@DisplayName("PRF-04: Tạo thông báo mới, đếm thông báo chưa đọc và đánh dấu tất cả đã đọc")
	void testNotificationLifecycle() {
		UserDtls user = new UserDtls();
		user.setName("Khách Nhận Thông Báo");
		user.setEmail("noti_" + UUID.randomUUID().toString().substring(0, 6) + "@test.com");
		user.setPassword("123456");
		user = userService.saveUser(user);

		notificationService.createNotification(user, "Đặt hàng thành công", "Đơn hàng #12345 đã được ghi nhận", "ORDER", "/user/user-orders");
		notificationService.createNotification(user, "Ưu đãi VIP", "Tặng mã giảm giá 10%", "PROMO", "/products");

		assertEquals(2L, notificationService.getUnreadCount(user.getId()), "Phải có 2 thông báo chưa đọc");

		List<Notification> list = notificationService.getUserNotifications(user.getId());
		assertEquals(2, list.size());

		// Đánh dấu 1 thông báo đã đọc
		notificationService.markAsRead(list.get(0).getId());
		assertEquals(1L, notificationService.getUnreadCount(user.getId()), "Số thông báo chưa đọc phải giảm còn 1");

		// Đánh dấu tất cả đã đọc
		notificationService.markAllAsRead(user.getId());
		assertEquals(0L, notificationService.getUnreadCount(user.getId()), "Sau khi đánh dấu tất cả, số chưa đọc phải bằng 0");
	}

	@Test
	@DisplayName("CHAT-01 -> CHAT-03: Lưu trữ tin nhắn 1-on-1, cô lập phòng chat giữa các khách hàng và đánh dấu đã đọc")
	void testPrivateLiveChatIsolationAndReadStatus() {
		String customerA = "custA_" + UUID.randomUUID().toString().substring(0, 5) + "@test.com";
		String customerB = "custB_" + UUID.randomUUID().toString().substring(0, 5) + "@test.com";

		// Khách A gửi 2 tin nhắn, Admin trả lời 1 tin nhắn cho Khách A
		supportMessageService.saveMessage(customerA, "Khách Hàng A", "Khách Hàng A", "ADMIN", "USER", "Chào shop, mình cần tư vấn MacBook");
		supportMessageService.saveMessage(customerA, "Khách Hàng A", "Admin CSKH", customerA, "ROLE_ADMIN", "Dạ chào anh A, shop sẵn hàng ạ!");

		// Khách B gửi 1 tin nhắn riêng
		supportMessageService.saveMessage(customerB, "Khách Hàng B", "Khách Hàng B", "ADMIN", "USER", "Shop có giao hỏa tốc Hà Nội không?");

		// Kiểm tra cô lập phòng chat: Lịch sử của Khách A không chứa tin nhắn của Khách B
		List<SupportMessage> historyA = supportMessageService.getConversation(customerA);
		List<SupportMessage> historyB = supportMessageService.getConversation(customerB);

		assertEquals(2, historyA.size(), "Phòng chat của Khách A phải có đúng 2 tin nhắn");
		assertEquals(1, historyB.size(), "Phòng chat của Khách B phải có đúng 1 tin nhắn");
		assertTrue(historyA.stream().allMatch(m -> customerA.equals(m.getCustomerId())), "Tuyệt đối không rò rỉ tin nhắn giữa Khách A và Khách B");

		// Kiểm tra danh sách hội thoại cho Admin
		List<Map<String, Object>> conversations = supportMessageService.getConversationsList();
		assertFalse(conversations.isEmpty());

		// Đánh dấu hội thoại của Khách A là đã đọc
		supportMessageService.markConversationAsRead(customerA);
		List<SupportMessage> updatedHistoryA = supportMessageService.getConversation(customerA);
		assertTrue(updatedHistoryA.get(0).getIsRead(), "Tin nhắn của Khách A phải được đánh dấu đã đọc");
	}

	@Test
	@DisplayName("ADM-01: Kiểm tra các hàm thống kê KPI và Biểu đồ trên Bảng điều khiển Quản trị (Admin Dashboard)")
	void testAdminDashboardAnalyticsQueries() {
		Double totalRevenue = orderService.getTotalRevenue();
		Long totalOrders = orderService.countOrders();
		Long totalProducts = productService.countProducts();
		Long lowStockCount = productService.countLowStock(10);
		List<Object[]> revenueByDate = orderService.getRevenueByDate();
		List<Object[]> statusCounts = orderService.getOrderStatusCounts();
		List<Object[]> topProducts = orderService.getTopSellingProducts(5);

		assertNotNull(totalRevenue, "Tổng doanh thu không được null");
		assertTrue(totalRevenue >= 0.0, "Tổng doanh thu phải >= 0");
		assertNotNull(totalOrders);
		assertNotNull(totalProducts);
		assertNotNull(lowStockCount);
		assertNotNull(revenueByDate);
		assertNotNull(statusCounts);
		assertNotNull(topProducts);
	}
}
