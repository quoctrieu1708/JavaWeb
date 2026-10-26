package com.ecom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.EntityManager;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.ecom.exception.InsufficientStockException;
import com.ecom.model.OrderRequest;
import com.ecom.model.Product;
import com.ecom.model.ProductOrder;
import com.ecom.model.UserDtls;
import com.ecom.model.Voucher;
import com.ecom.repository.ProductRepository;
import com.ecom.service.CartService;
import com.ecom.service.OrderService;
import com.ecom.service.PdfInvoiceService;
import com.ecom.service.ProductService;
import com.ecom.service.UserService;
import com.ecom.service.VoucherService;
import com.ecom.util.OrderStatus;

/**
 * WORKFLOW TEST NHÓM 4: Mã giảm giá Voucher, Đặt hàng nguyên tử (@Transactional),
 * Chống bán vượt kho, Hoàn kho khi hủy đơn & Xuất Hóa đơn PDF.
 * Thay thế thao tác thủ công cho các kịch bản ORD-01 -> ORD-05 và HIS-01 -> HIS-04.
 */
@SpringBootTest
@Transactional
class OrderVoucherAndInvoiceWorkflowTest {

	@Autowired
	private OrderService orderService;

	@Autowired
	private VoucherService voucherService;

	@Autowired
	private CartService cartService;

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private PdfInvoiceService pdfInvoiceService;

	@Autowired
	private EntityManager entityManager;

	private UserDtls createTestUser() {
		UserDtls u = new UserDtls();
		u.setName("Khách Đặt Hàng VIP");
		u.setEmail("order_" + UUID.randomUUID().toString().substring(0, 6) + "@test.com");
		u.setMobileNumber("0909112233");
		u.setPassword("123456");
		return userService.saveUser(u);
	}

	private Product createTestProduct(String name, double price, int stock) {
		Product p = new Product();
		p.setTitle(name);
		p.setCategory("Laptop");
		p.setPrice(price);
		p.setDiscount(0);
		p.setDiscountPrice(price);
		p.setStock(stock);
		p.setIsActive(true);
		p.setImage("laptop.png");
		return productService.saveProduct(p);
	}

	@Test
	@DisplayName("ORD-02 & ORD-03: Kiểm tra tính hợp lệ của Voucher (còn hạn, đơn tối thiểu, tính chiết khấu %)")
	void testVoucherValidationAndDiscountCalculation() {
		String code = "VIP" + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
		Voucher v = new Voucher();
		v.setCode(code);
		v.setDescription("Giảm 10% cho đơn từ 10 triệu");
		v.setDiscountPercent(10);
		v.setMinOrderValue(10_000_000.0);
		v.setUsageLimit(5);
		v.setUsedCount(0);
		v.setStartDate(LocalDate.now().minusDays(1));
		v.setEndDate(LocalDate.now().plusDays(7));
		v.setIsActive(true);
		voucherService.saveVoucher(v);

		// Đơn hàng 5 triệu (< 10 triệu tối thiểu) -> Không hợp lệ
		Voucher invalidMinOrder = voucherService.validateAndGetVoucher(code, 5_000_000.0);
		assertNull(invalidMinOrder, "Đơn hàng chưa đạt giá trị tối thiểu không được áp dụng voucher");

		// Đơn hàng 20 triệu (>= 10 triệu) -> Hợp lệ, giảm 10% = 2 triệu
		Voucher validVoucher = voucherService.validateAndGetVoucher(code, 20_000_000.0);
		assertNotNull(validVoucher, "Đơn hàng đủ điều kiện phải lấy được voucher hợp lệ");
		Double discount = voucherService.calculateDiscount(validVoucher, 20_000_000.0);
		assertEquals(2_000_000.0, discount, 0.01, "Mức giảm 10% của 20 triệu phải là 2 triệu");
	}

	@Test
	@DisplayName("ORD-04, HIS-01 & HIS-03: Đặt hàng nguyên tử trừ kho, Xuất Hóa đơn PDF và Tự động hoàn kho khi Hủy đơn")
	void testAtomicOrderPlacementPdfInvoiceAndStockRestorationOnCancel() throws Exception {
		UserDtls user = createTestUser();
		Product p1 = createTestProduct("Dell XPS 16 OLED", 40_000_000.0, 10);
		Product p2 = createTestProduct("Chuột Logitech MX Master 3S", 2_500_000.0, 5);

		// Thêm vào giỏ: 2 chiếc p1 và 1 chiếc p2
		cartService.saveCart(p1.getId(), user.getId(), 2);
		cartService.saveCart(p2.getId(), user.getId(), 1);

		OrderRequest req = new OrderRequest();
		req.setFullName("Nguyễn Văn Mua Hàng");
		req.setEmail(user.getEmail());
		req.setPhone("0912345678");
		req.setProvince("TP Hồ Chí Minh");
		req.setWard("Phường Bến Nghé");
		req.setDetailAddress("Tòa nhà Bitexco, Quận 1");
		req.setPaymentType("COD");

		// 1. Thực hiện đặt hàng
		orderService.saveOrder(user.getId(), req);
		entityManager.flush();
		entityManager.clear();

		// Kiểm tra giỏ hàng đã được làm trống
		assertEquals(0, cartService.getCartsByUser(user.getId()).size(), "Giỏ hàng phải trống sau khi đặt hàng thành công");

		// Kiểm tra tồn kho đã giảm chính xác: p1 từ 10 -> 8, p2 từ 5 -> 4
		Product p1AfterOrder = productService.getProductById(p1.getId());
		Product p2AfterOrder = productService.getProductById(p2.getId());
		assertEquals(8, p1AfterOrder.getStock(), "Tồn kho SP1 phải giảm từ 10 xuống 8");
		assertEquals(4, p2AfterOrder.getStock(), "Tồn kho SP2 phải giảm từ 5 xuống 4");

		// Kiểm tra đơn hàng được tạo với cùng 1 mã orderId
		List<ProductOrder> userOrders = orderService.getOrdersByUser(user.getId());
		assertEquals(2, userOrders.size(), "Phải có 2 dòng chi tiết đơn hàng");
		String sharedOrderId = userOrders.get(0).getOrderId();
		assertEquals(sharedOrderId, userOrders.get(1).getOrderId(), "Các sản phẩm cùng lần đặt phải dùng chung mã orderId");

		// 2. Kiểm tra xuất Hóa đơn PDF VAT
		List<ProductOrder> invoiceItems = orderService.getOrderItemsByOrderId(sharedOrderId);
		byte[] pdfBytes = pdfInvoiceService.generateInvoicePdf(invoiceItems);
		assertNotNull(pdfBytes);
		assertTrue(pdfBytes.length > 500, "File PDF hóa đơn kết xuất phải có dung lượng hợp lệ (> 500 bytes)");

		// 3. Kiểm tra Hủy đơn hàng (OrderStatus.CANCEL) -> Tự động hoàn trả tồn kho
		for (ProductOrder orderItem : userOrders) {
			orderService.updateOrderStatus(orderItem.getId(), OrderStatus.CANCEL.getName());
		}
		entityManager.flush();
		entityManager.clear();

		Product p1AfterCancel = productService.getProductById(p1.getId());
		Product p2AfterCancel = productService.getProductById(p2.getId());
		assertEquals(10, p1AfterCancel.getStock(), "Sau khi hủy đơn, tồn kho SP1 phải được hoàn lại đủ 10");
		assertEquals(5, p2AfterCancel.getStock(), "Sau khi hủy đơn, tồn kho SP2 phải được hoàn lại đủ 5");
	}

	@Test
	@DisplayName("ORD-05: Chặn bán vượt tồn kho (InsufficientStockException) khi sản phẩm hết hàng ngay lúc bấm thanh toán")
	void testInsufficientStockThrowsExceptionDuringCheckout() {
		UserDtls user = createTestUser();
		Product product = createTestProduct("PlayStation 5 Pro Limited", 20_000_000.0, 5);

		// Khách thêm 4 chiếc vào giỏ khi kho đang có 5
		cartService.saveCart(product.getId(), user.getId(), 4);

		// Giả lập người khác vừa mua mất 3 chiếc -> Kho chỉ còn 2 chiếc (< 4 chiếc trong giỏ)
		productRepository.decreaseStock(product.getId(), 3);
		entityManager.flush();
		entityManager.clear();

		OrderRequest req = new OrderRequest();
		req.setFullName("Khách Hàng Chậm Chân");
		req.setEmail(user.getEmail());
		req.setPhone("0912345678");
		req.setProvince("Hà Nội");
		req.setWard("Phường Dịch Vọng");
		req.setDetailAddress("Cầu Giấy");
		req.setPaymentType("COD");

		// Khi bấm đặt hàng 4 chiếc trong khi kho chỉ còn 2 -> Phải ném InsufficientStockException
		assertThrows(InsufficientStockException.class, () -> {
			orderService.saveOrder(user.getId(), req);
		}, "Phải ném InsufficientStockException khi tồn kho thực tế không đủ đáp ứng số lượng trong giỏ");
	}
}
