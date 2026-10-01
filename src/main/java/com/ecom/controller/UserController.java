package com.ecom.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ecom.model.Cart;
import com.ecom.model.Category;
import com.ecom.model.OrderRequest;
import com.ecom.model.ProductOrder;
import com.ecom.model.UserDtls;
import com.ecom.repository.UserRepository;
import com.ecom.service.CartService;
import com.ecom.service.CategoryService;
import com.ecom.service.OrderService;
import com.ecom.service.UserService;
import com.ecom.util.CommonUtil;
import com.ecom.util.OrderStatus;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;
	@Autowired
	private CategoryService categoryService;

	@Autowired
	private CartService cartService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private CommonUtil commonUtil;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private com.ecom.service.ReviewService reviewService;

	@Autowired
	private com.ecom.service.VoucherService voucherService;

	@Autowired
	private com.ecom.service.NotificationService notificationService;

	@Autowired
	private com.ecom.service.PdfInvoiceService pdfInvoiceService;

	@Autowired
	private com.ecom.service.ProductService productService;

	@Autowired
	private com.ecom.repository.ProductOrderRepository productOrderRepository;

	@GetMapping("/")
	public String home() {
		return "user/home";
	}

	@ModelAttribute
	public void getUserDetails(Principal p, Model m) {
		if (p != null) {
			String email = p.getName();
			UserDtls userDtls = userService.getUserByEmail(email);
			m.addAttribute("user", userDtls);
			Integer countCart = cartService.getCountCart(userDtls.getId());
			m.addAttribute("countCart", countCart);
			Long countNotification = notificationService.getUnreadCount(userDtls.getId());
			m.addAttribute("countNotification", countNotification != null ? countNotification : 0L);
		}

		List<Category> allActiveCategory = categoryService.getAllActiveCategory();
		m.addAttribute("categorys", allActiveCategory);
	}

	@GetMapping("/addCart")
	public String addToCart(@RequestParam Integer pid, 
			@RequestParam(name = "quantity", defaultValue = "1") Integer quantity, 
			Principal p, HttpSession session) {
		UserDtls loggedInUser = getLoggedInUserDetails(p);
		Integer uid = loggedInUser.getId();

		com.ecom.model.Product product = productService.getProductById(pid);
		if (product == null || product.getStock() <= 0) {
			session.setAttribute("errorMsg", "Sản phẩm tạm thời hết hàng trong kho!");
			return "redirect:/product/" + pid;
		}

		if (quantity == null || quantity <= 0) {
			quantity = 1;
		}

		Cart existingCart = cartService.getCartByProductAndUser(pid, uid);
		int currentInCart = (existingCart != null && existingCart.getQuantity() != null) ? existingCart.getQuantity() : 0;
		int availableStock = product.getStock();

		if (currentInCart >= availableStock) {
			session.setAttribute("errorMsg", "Sản phẩm này đã đạt số lượng tối đa trong giỏ hàng (tồn kho hiện có: " + availableStock + ")!");
			return "redirect:/product/" + pid;
		}

		int allowedAdd = availableStock - currentInCart;
		int actualAdd = Math.min(quantity, allowedAdd);

		Cart saveCart = cartService.saveCart(pid, uid, actualAdd);

		if (ObjectUtils.isEmpty(saveCart)) {
			session.setAttribute("errorMsg", "Thêm sản phẩm vào giỏ hàng thất bại.");
		} else {
			int newTotalInCart = saveCart.getQuantity();
			if (existingCart != null) {
				if (actualAdd < quantity) {
					session.setAttribute("succMsg", "Sản phẩm đã có trong giỏ hàng. Đã thêm " + actualAdd + " sản phẩm (tổng cộng " + newTotalInCart + " sản phẩm, đã đạt tối đa tồn kho)!");
				} else {
					session.setAttribute("succMsg", "Sản phẩm đã có trong giỏ hàng. Đã cập nhật tăng " + actualAdd + " sản phẩm (tổng cộng " + newTotalInCart + " sản phẩm trong giỏ)!");
				}
			} else {
				if (actualAdd < quantity) {
					session.setAttribute("succMsg", "Đã thêm " + actualAdd + " sản phẩm vào giỏ hàng (đạt giới hạn kho " + availableStock + ")!");
				} else {
					session.setAttribute("succMsg", "Đã thêm " + actualAdd + " sản phẩm vào giỏ hàng thành công!");
				}
			}
		}
		return "redirect:/product/" + pid;
	}

	@GetMapping("/cart")
	public String loadCartPage(Principal p, Model m) {

		UserDtls user = getLoggedInUserDetails(p);
		List<Cart> carts = cartService.getCartsByUser(user.getId());
		m.addAttribute("carts", carts);
		if (carts.size() > 0) {
			Double totalOrderPrice = carts.get(carts.size() - 1).getTotalOrderPrice();
			m.addAttribute("totalOrderPrice", totalOrderPrice);
		}
		return "/user/cart";
	}

	@GetMapping("/cartQuantityUpdate")
	public String updateCartQuantity(@RequestParam String sy, @RequestParam Integer cid, HttpSession session) {
		cartService.updateQuantity(sy, cid);
		return "redirect:/user/cart";
	}

	@GetMapping("/cart/update-quantity-direct")
	public String updateCartQuantityDirect(@RequestParam Integer cid, @RequestParam Integer quantity, HttpSession session) {
		cartService.updateQuantityDirect(cid, quantity);
		session.setAttribute("succMsg", "Đã cập nhật số lượng sản phẩm trong giỏ hàng.");
		return "redirect:/user/cart";
	}

	private UserDtls getLoggedInUserDetails(Principal p) {
		String email = p.getName();
		UserDtls userDtls = userService.getUserByEmail(email);
		return userDtls;
	}

	@GetMapping("/orders")
	public String orderPage(Principal p, Model m) {
		UserDtls user = getLoggedInUserDetails(p);
		List<Cart> carts = cartService.getCartsByUser(user.getId());
		m.addAttribute("carts", carts);
		if (carts.size() > 0) {
			Double orderPrice = carts.get(carts.size() - 1).getTotalOrderPrice();
			Double shippingFee = 30000.0;
			Double tax = 10000.0;
			Double totalOrderPrice = orderPrice + shippingFee + tax;
			m.addAttribute("orderPrice", orderPrice);
			m.addAttribute("shippingFee", shippingFee);
			m.addAttribute("tax", tax);
			m.addAttribute("totalOrderPrice", totalOrderPrice);
		}
		return "/user/order";
	}

	@PostMapping("/save-order")
	public String saveOrder(@ModelAttribute OrderRequest request, Principal p) throws Exception {
		// System.out.println(request);
		UserDtls user = getLoggedInUserDetails(p);
		orderService.saveOrder(user.getId(), request);

		return "redirect:/user/success";
	}

	@GetMapping("/success")
	public String loadSuccess() {
		return "/user/success";
	}

	@GetMapping("/user-orders")
	public String myOrder(Model m, Principal p) {
		UserDtls loginUser = getLoggedInUserDetails(p);
		List<ProductOrder> orders = orderService.getOrdersByUser(loginUser.getId());
		m.addAttribute("orders", orders);
		return "/user/my_orders";
	}

	@GetMapping("/update-status")
	public String updateOrderStatus(@RequestParam Integer id, @RequestParam Integer st, HttpSession session) {

		OrderStatus[] values = OrderStatus.values();
		String status = null;

		for (OrderStatus orderSt : values) {
			if (orderSt.getId().equals(st)) {
				status = orderSt.getName();
			}
		}

		ProductOrder updateOrder = orderService.updateOrderStatus(id, status);
		
		try {
			commonUtil.sendMailForProductOrder(updateOrder, status);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (!ObjectUtils.isEmpty(updateOrder)) {
			session.setAttribute("succMsg", "Cập nhật trạng thái thành công");
		} else {
			session.setAttribute("errorMsg", "Không thể cập nhật trạng thái");
		}
		return "redirect:/user/user-orders";
	}

	@GetMapping("/profile")
	public String profile() {
		return "/user/profile";
	}

	@PostMapping("/update-profile")
	public String updateProfile(@ModelAttribute UserDtls user, @RequestParam MultipartFile img, HttpSession session) {
		UserDtls updateUserProfile = userService.updateUserProfile(user, img);
		if (ObjectUtils.isEmpty(updateUserProfile)) {
			session.setAttribute("errorMsg", "Cập nhật hồ sơ thất bại");
		} else {
			session.setAttribute("succMsg", "Cập nhật hồ sơ thành công");
		}
		return "redirect:/user/profile";
	}

	@PostMapping("/change-password")
	public String changePassword(@RequestParam String newPassword, @RequestParam String currentPassword, Principal p,
			HttpSession session) {
		UserDtls loggedInUserDetails = getLoggedInUserDetails(p);

		boolean matches = passwordEncoder.matches(currentPassword, loggedInUserDetails.getPassword());

		if (matches) {
			String encodePassword = passwordEncoder.encode(newPassword);
			loggedInUserDetails.setPassword(encodePassword);
			UserDtls updateUser = userService.updateUser(loggedInUserDetails);
			if (ObjectUtils.isEmpty(updateUser)) {
				session.setAttribute("errorMsg", "Đổi mật khẩu thất bại! Lỗi máy chủ");
			} else {
				session.setAttribute("succMsg", "Đổi mật khẩu thành công");
			}
		} else {
			session.setAttribute("errorMsg", "Mật khẩu hiện tại không chính xác");
		}

		return "redirect:/user/profile";
	}

	@PostMapping("/addReview")
	public String addReview(@RequestParam Integer productId, @RequestParam Integer rating,
			@RequestParam String comment, Principal p, HttpSession session) {
		UserDtls user = getLoggedInUserDetails(p);
		com.ecom.model.Review review = reviewService.saveReview(productId, user.getId(), rating, comment);
		if (review != null) {
			session.setAttribute("succMsg", "Cảm ơn bạn đã gửi đánh giá sản phẩm!");
		} else {
			session.setAttribute("errorMsg", "Gửi đánh giá thất bại.");
		}
		return "redirect:/product/" + productId;
	}

	@GetMapping(value = "/validate-voucher", produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	@org.springframework.web.bind.annotation.ResponseBody
	public org.springframework.http.ResponseEntity<String> validateVoucher(@RequestParam String code, @RequestParam Double total) {
		com.ecom.model.Voucher voucher = voucherService.validateAndGetVoucher(code, total);
		if (voucher == null) {
			String json = "{\"valid\":false,\"message\":\"Mã giảm giá không hợp lệ, chưa tới hạn hoặc đã hết lượt dùng!\"}";
			return org.springframework.http.ResponseEntity.ok()
					.contentType(org.springframework.http.MediaType.APPLICATION_JSON)
					.body(json);
		}
		Double discount = voucherService.calculateDiscount(voucher, total);
		String desc = voucher.getDescription() != null ? voucher.getDescription().replace("\"", "\\\"") : "";
		String json = String.format(java.util.Locale.US,
				"{\"valid\":true,\"code\":\"%s\",\"discount\":%.0f,\"description\":\"%s\",\"message\":\"Áp dụng mã giảm giá thành công!\"}",
				voucher.getCode(), discount, desc);
		return org.springframework.http.ResponseEntity.ok()
				.contentType(org.springframework.http.MediaType.APPLICATION_JSON)
				.body(json);
	}

	@GetMapping("/notifications")
	public String notifications(Principal p, Model m) {
		UserDtls user = getLoggedInUserDetails(p);
		List<com.ecom.model.Notification> list = notificationService.getUserNotifications(user.getId());
		m.addAttribute("notifications", list);
		return "/user/notifications";
	}

	@GetMapping("/notification-read")
	public String markNotificationRead(@RequestParam Integer id, @RequestParam(required = false) String redirectUrl) {
		notificationService.markAsRead(id);
		if (redirectUrl != null && !redirectUrl.isEmpty()) {
			return "redirect:" + redirectUrl;
		}
		return "redirect:/user/notifications";
	}

	@GetMapping("/notifications-read-all")
	public String markAllNotificationsRead(Principal p) {
		UserDtls user = getLoggedInUserDetails(p);
		notificationService.markAllAsRead(user.getId());
		return "redirect:/user/notifications";
	}

	@GetMapping("/order/invoice/{orderId}")
	public org.springframework.http.ResponseEntity<byte[]> downloadInvoice(@PathVariable String orderId, Principal p) {
		UserDtls user = getLoggedInUserDetails(p);
		List<ProductOrder> orders = productOrderRepository.findAllByOrderId(orderId);
		if (orders.isEmpty()) {
			// Try finding by single orderId
			ProductOrder single = productOrderRepository.findByOrderId(orderId);
			if (single != null) {
				orders = java.util.Collections.singletonList(single);
			}
		}
		if (orders.isEmpty() || !orders.get(0).getUser().getId().equals(user.getId())) {
			return org.springframework.http.ResponseEntity.status(403).build();
		}
		try {
			byte[] pdf = pdfInvoiceService.generateInvoicePdf(orders);
			return org.springframework.http.ResponseEntity.ok()
					.header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice_" + orderId.substring(0, Math.min(8, orderId.length())) + ".pdf")
					.contentType(org.springframework.http.MediaType.APPLICATION_PDF)
					.body(pdf);
		} catch (Exception e) {
			return org.springframework.http.ResponseEntity.internalServerError().build();
		}
	}

}
