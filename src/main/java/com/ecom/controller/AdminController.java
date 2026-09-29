package com.ecom.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
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

import com.ecom.model.Category;
import com.ecom.model.Product;
import com.ecom.model.ProductOrder;
import com.ecom.model.UserDtls;
import com.ecom.service.CartService;
import com.ecom.service.CategoryService;
import com.ecom.service.OrderService;
import com.ecom.service.ProductService;
import com.ecom.service.UserService;
import com.ecom.util.CommonUtil;
import com.ecom.util.OrderStatus;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private ProductService productService;

	@Autowired
	private UserService userService;

	@Autowired
	private CartService cartService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private CommonUtil commonUtil;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private com.ecom.repository.ProductOrderRepository productOrderRepository;

	@Autowired
	private com.ecom.repository.ProductRepository productRepository;

	@Autowired
	private com.ecom.repository.UserRepository userRepository;

	@Autowired
	private com.ecom.service.VoucherService voucherService;

	@Autowired
	private com.ecom.service.PdfInvoiceService pdfInvoiceService;

	@ModelAttribute
	public void getUserDetails(Principal p, Model m) {
		if (p != null) {
			String email = p.getName();
			UserDtls userDtls = userService.getUserByEmail(email);
			m.addAttribute("user", userDtls);
			Integer countCart = cartService.getCountCart(userDtls.getId());
			m.addAttribute("countCart", countCart);
		}

		List<Category> allActiveCategory = categoryService.getAllActiveCategory();
		m.addAttribute("categorys", allActiveCategory);
	}

	@GetMapping("/")
	public String index(Model m) {
		Double totalRevenue = productOrderRepository.getTotalRevenue();
		Long totalOrders = productOrderRepository.count();
		Long pendingOrders = productOrderRepository.countByStatus(OrderStatus.IN_PROGRESS.getName());
		Long totalProducts = productRepository.count();
		Long lowStockCount = productService.countLowStock(10);
		Long totalUsers = userRepository.countByRole("ROLE_USER");

		m.addAttribute("totalRevenue", totalRevenue != null ? totalRevenue : 0.0);
		m.addAttribute("totalOrders", totalOrders != null ? totalOrders : 0L);
		m.addAttribute("pendingOrders", pendingOrders != null ? pendingOrders : 0L);
		m.addAttribute("totalProducts", totalProducts != null ? totalProducts : 0L);
		m.addAttribute("lowStockCount", lowStockCount != null ? lowStockCount : 0L);
		m.addAttribute("totalUsers", totalUsers != null ? totalUsers : 0L);

		// Revenue chart data (date vs sum)
		List<Object[]> revenueByDate = productOrderRepository.getRevenueByDate();
		java.util.List<String> chartDates = new java.util.ArrayList<>();
		java.util.List<Double> chartRevenues = new java.util.ArrayList<>();
		if (revenueByDate != null) {
			for (Object[] row : revenueByDate) {
				chartDates.add(row[0] != null ? row[0].toString() : "");
				chartRevenues.add(row[1] != null ? ((Number) row[1]).doubleValue() : 0.0);
			}
		}
		m.addAttribute("chartDates", chartDates);
		m.addAttribute("chartRevenues", chartRevenues);

		// Order status counts for Doughnut chart
		List<Object[]> statusCounts = productOrderRepository.getOrderStatusCounts();
		java.util.List<String> statusLabels = new java.util.ArrayList<>();
		java.util.List<Long> statusData = new java.util.ArrayList<>();
		if (statusCounts != null) {
			for (Object[] row : statusCounts) {
				statusLabels.add(row[0] != null ? row[0].toString() : "Khác");
				statusData.add(row[1] != null ? ((Number) row[1]).longValue() : 0L);
			}
		}
		m.addAttribute("statusLabels", statusLabels);
		m.addAttribute("statusData", statusData);

		// Recent orders
		List<ProductOrder> allOrders = orderService.getAllOrders();
		List<ProductOrder> recentOrders = allOrders.stream()
				.sorted((o1, o2) -> o2.getId().compareTo(o1.getId()))
				.limit(5)
				.toList();
		m.addAttribute("recentOrders", recentOrders);

		// Top selling products
		List<Object[]> topProducts = productOrderRepository.getTopSellingProducts(org.springframework.data.domain.PageRequest.of(0, 5));
		m.addAttribute("topProducts", topProducts);

		return "admin/index";
	}

	@GetMapping("/loadAddProduct")
	public String loadAddProduct(Model m) {
		List<Category> categories = categoryService.getAllCategory();
		m.addAttribute("categories", categories);
		return "admin/add_product";
	}

	@GetMapping("/category")
	public String category(Model m, @RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
		// m.addAttribute("categorys", categoryService.getAllCategory());
		Page<Category> page = categoryService.getAllCategorPagination(pageNo, pageSize);
		List<Category> categorys = page.getContent();
		m.addAttribute("categorys", categorys);

		m.addAttribute("pageNo", page.getNumber());
		m.addAttribute("pageSize", pageSize);
		m.addAttribute("totalElements", page.getTotalElements());
		m.addAttribute("totalPages", page.getTotalPages());
		m.addAttribute("isFirst", page.isFirst());
		m.addAttribute("isLast", page.isLast());

		return "admin/category";
	}

	@PostMapping("/saveCategory")
	public String saveCategory(@ModelAttribute Category category, @RequestParam("file") MultipartFile file,
			HttpSession session) throws IOException {

		String imageName = file != null ? file.getOriginalFilename() : "default.jpg";
		category.setImageName(imageName);

		Boolean existCategory = categoryService.existCategory(category.getName());

		if (existCategory) {
			session.setAttribute("errorMsg", "Tên danh mục đã tồn tại");
		} else {

			Category saveCategory = categoryService.saveCategory(category);

			if (ObjectUtils.isEmpty(saveCategory)) {
				session.setAttribute("errorMsg", "Lưu thất bại! Lỗi máy chủ");
			} else {

				File saveFile = new ClassPathResource("static/img").getFile();

				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "category_img" + File.separator
						+ file.getOriginalFilename());

				// System.out.println(path);
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

				session.setAttribute("succMsg", "Lưu thành công");
			}
		}

		return "redirect:/admin/category";
	}

	@GetMapping("/deleteCategory/{id}")
	public String deleteCategory(@PathVariable int id, HttpSession session) {
		Boolean deleteCategory = categoryService.deleteCategory(id);

		if (deleteCategory) {
			session.setAttribute("succMsg", "Xóa danh mục thành công");
		} else {
			session.setAttribute("errorMsg", "Đã xảy ra lỗi trên máy chủ");
		}

		return "redirect:/admin/category";
	}

	@GetMapping("/loadEditCategory/{id}")
	public String loadEditCategory(@PathVariable int id, Model m) {
		m.addAttribute("category", categoryService.getCategoryById(id));
		return "admin/edit_category";
	}

	@PostMapping("/updateCategory")
	public String updateCategory(@ModelAttribute Category category, @RequestParam("file") MultipartFile file,
			HttpSession session) throws IOException {

		Category oldCategory = categoryService.getCategoryById(category.getId());
		String imageName = file.isEmpty() ? oldCategory.getImageName() : file.getOriginalFilename();

		if (!ObjectUtils.isEmpty(category)) {

			oldCategory.setName(category.getName());
			oldCategory.setIsActive(category.getIsActive());
			oldCategory.setImageName(imageName);
		}

		Category updateCategory = categoryService.saveCategory(oldCategory);

		if (!ObjectUtils.isEmpty(updateCategory)) {

			if (!file.isEmpty()) {
				File saveFile = new ClassPathResource("static/img").getFile();

				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "category_img" + File.separator
						+ file.getOriginalFilename());

				// System.out.println(path);
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			}

			session.setAttribute("succMsg", "Cập nhật danh mục thành công");
		} else {
			session.setAttribute("errorMsg", "Đã xảy ra lỗi trên máy chủ");
		}

		return "redirect:/admin/loadEditCategory/" + category.getId();
	}

	@PostMapping("/saveProduct")
	public String saveProduct(@ModelAttribute Product product, @RequestParam("file") MultipartFile image,
			HttpSession session) throws IOException {

		String imageName = image.isEmpty() ? "default.jpg" : image.getOriginalFilename();

		product.setImage(imageName);
		product.setDiscount(0);
		product.setDiscountPrice(product.getPrice());
		Product saveProduct = productService.saveProduct(product);

		if (!ObjectUtils.isEmpty(saveProduct)) {

			File saveFile = new ClassPathResource("static/img").getFile();

			Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "product_img" + File.separator
					+ image.getOriginalFilename());

			// System.out.println(path);
			Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

			session.setAttribute("succMsg", "Thêm sản phẩm thành công");
		} else {
			session.setAttribute("errorMsg", "Đã xảy ra lỗi trên máy chủ");
		}

		return "redirect:/admin/loadAddProduct";
	}

	@GetMapping("/products")
	public String loadViewProduct(Model m, @RequestParam(defaultValue = "") String ch,
			@RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {

//		List<Product> products = null;
//		if (ch != null && ch.length() > 0) {
//			products = productService.searchProduct(ch);
//		} else {
//			products = productService.getAllProducts();
//		}
//		m.addAttribute("products", products);

		Page<Product> page = null;
		if (ch != null && ch.length() > 0) {
			page = productService.searchProductPagination(pageNo, pageSize, ch);
		} else {
			page = productService.getAllProductsPagination(pageNo, pageSize);
		}
		m.addAttribute("products", page.getContent());
		m.addAttribute("lowStockCount", productService.countLowStock(10));
		m.addAttribute("outOfStockCount", productService.countOutOfStock());

		m.addAttribute("pageNo", page.getNumber());
		m.addAttribute("pageSize", pageSize);
		m.addAttribute("totalElements", page.getTotalElements());
		m.addAttribute("totalPages", page.getTotalPages());
		m.addAttribute("isFirst", page.isFirst());
		m.addAttribute("isLast", page.isLast());

		return "admin/products";
	}

	@GetMapping("/deleteProduct/{id}")
	public String deleteProduct(@PathVariable int id, HttpSession session) {
		Boolean deleteProduct = productService.deleteProduct(id);
		if (deleteProduct) {
			session.setAttribute("succMsg", "Xóa sản phẩm thành công");
		} else {
			session.setAttribute("errorMsg", "Đã xảy ra lỗi trên máy chủ");
		}
		return "redirect:/admin/products";
	}

	@GetMapping("/editProduct/{id}")
	public String editProduct(@PathVariable int id, Model m) {
		m.addAttribute("product", productService.getProductById(id));
		m.addAttribute("categories", categoryService.getAllCategory());
		return "admin/edit_product";
	}

	@PostMapping("/updateProduct")
	public String updateProduct(@ModelAttribute Product product, @RequestParam("file") MultipartFile image,
			HttpSession session, Model m) {

		if (product.getDiscount() < 0 || product.getDiscount() > 100) {
			session.setAttribute("errorMsg", "Mức giảm giá không hợp lệ (0-100%)");
		} else {
			Product updateProduct = productService.updateProduct(product, image);
			if (!ObjectUtils.isEmpty(updateProduct)) {
				session.setAttribute("succMsg", "Cập nhật sản phẩm thành công");
			} else {
				session.setAttribute("errorMsg", "Đã xảy ra lỗi trên máy chủ");
			}
		}
		return "redirect:/admin/editProduct/" + product.getId();
	}

	@GetMapping("/users")
	public String getAllUsers(Model m, @RequestParam Integer type) {
		List<UserDtls> users = null;
		if (type == 1) {
			users = userService.getUsers("ROLE_USER");
		} else {
			users = userService.getUsers("ROLE_ADMIN");
		}
		m.addAttribute("userType",type);
		m.addAttribute("users", users);
		return "/admin/users";
	}

	@GetMapping("/updateSts")
	public String updateUserAccountStatus(@RequestParam Boolean status, @RequestParam Integer id,@RequestParam Integer type, HttpSession session) {
		Boolean f = userService.updateAccountStatus(id, status);
		if (f) {
			session.setAttribute("succMsg", "Đã cập nhật trạng thái tài khoản");
		} else {
			session.setAttribute("errorMsg", "Đã xảy ra lỗi trên máy chủ");
		}
		return "redirect:/admin/users?type="+type;
	}

	@GetMapping("/orders")
	public String getAllOrders(Model m, @RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
//		List<ProductOrder> allOrders = orderService.getAllOrders();
//		m.addAttribute("orders", allOrders);
//		m.addAttribute("srch", false);

		Page<ProductOrder> page = orderService.getAllOrdersPagination(pageNo, pageSize);
		m.addAttribute("orders", page.getContent());
		m.addAttribute("srch", false);

		m.addAttribute("pageNo", page.getNumber());
		m.addAttribute("pageSize", pageSize);
		m.addAttribute("totalElements", page.getTotalElements());
		m.addAttribute("totalPages", page.getTotalPages());
		m.addAttribute("isFirst", page.isFirst());
		m.addAttribute("isLast", page.isLast());

		return "/admin/orders";
	}

	@PostMapping("/update-order-status")
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
		return "redirect:/admin/orders";
	}

	@GetMapping("/search-order")
	public String searchProduct(@RequestParam String orderId, Model m, HttpSession session,
			@RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {

		if (orderId != null && orderId.length() > 0) {

			ProductOrder order = orderService.getOrdersByOrderId(orderId.trim());

			if (ObjectUtils.isEmpty(order)) {
				session.setAttribute("errorMsg", "Mã đơn hàng không chính xác");
				m.addAttribute("orderDtls", null);
			} else {
				m.addAttribute("orderDtls", order);
			}

			m.addAttribute("srch", true);
		} else {
//			List<ProductOrder> allOrders = orderService.getAllOrders();
//			m.addAttribute("orders", allOrders);
//			m.addAttribute("srch", false);

			Page<ProductOrder> page = orderService.getAllOrdersPagination(pageNo, pageSize);
			m.addAttribute("orders", page);
			m.addAttribute("srch", false);

			m.addAttribute("pageNo", page.getNumber());
			m.addAttribute("pageSize", pageSize);
			m.addAttribute("totalElements", page.getTotalElements());
			m.addAttribute("totalPages", page.getTotalPages());
			m.addAttribute("isFirst", page.isFirst());
			m.addAttribute("isLast", page.isLast());

		}
		return "/admin/orders";

	}

	@GetMapping("/add-admin")
	public String loadAdminAdd() {
		return "/admin/add_admin";
	}

	@PostMapping("/save-admin")
	public String saveAdmin(@ModelAttribute UserDtls user, @RequestParam("img") MultipartFile file, HttpSession session)
			throws IOException {

		String imageName = file.isEmpty() ? "default.jpg" : file.getOriginalFilename();
		user.setProfileImage(imageName);
		UserDtls saveUser = userService.saveAdmin(user);

		if (!ObjectUtils.isEmpty(saveUser)) {
			if (!file.isEmpty()) {
				File saveFile = new ClassPathResource("static/img").getFile();

				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "profile_img" + File.separator
						+ file.getOriginalFilename());

//				System.out.println(path);
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			}
			session.setAttribute("succMsg", "Đăng ký tài khoản thành công");
		} else {
			session.setAttribute("errorMsg", "Đã xảy ra lỗi trên máy chủ");
		}

		return "redirect:/admin/add-admin";
	}

	@GetMapping("/profile")
	public String profile() {
		return "/admin/profile";
	}

	@PostMapping("/update-profile")
	public String updateProfile(@ModelAttribute UserDtls user, @RequestParam MultipartFile img, HttpSession session) {
		UserDtls updateUserProfile = userService.updateUserProfile(user, img);
		if (ObjectUtils.isEmpty(updateUserProfile)) {
			session.setAttribute("errorMsg", "Cập nhật hồ sơ thất bại");
		} else {
			session.setAttribute("succMsg", "Hồ sơ đã được cập nhật thành công");
		}
		return "redirect:/admin/profile";
	}

	@PostMapping("/change-password")
	public String changePassword(@RequestParam String newPassword, @RequestParam String currentPassword, Principal p,
			HttpSession session) {
		UserDtls loggedInUserDetails = commonUtil.getLoggedInUserDetails(p);

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

		return "redirect:/admin/profile";
	}

	@GetMapping("/vouchers")
	public String vouchers(Model m) {
		m.addAttribute("vouchers", voucherService.getAllVouchers());
		m.addAttribute("voucher", new com.ecom.model.Voucher());
		return "admin/vouchers";
	}

	@PostMapping("/save-voucher")
	public String saveVoucher(@ModelAttribute com.ecom.model.Voucher voucher, HttpSession session) {
		try {
			voucherService.saveVoucher(voucher);
			session.setAttribute("succMsg", "Lưu mã giảm giá thành công");
		} catch (Exception e) {
			session.setAttribute("errorMsg", "Không thể lưu mã giảm giá: " + e.getMessage());
		}
		return "redirect:/admin/vouchers";
	}

	@GetMapping("/delete-voucher/{id}")
	public String deleteVoucher(@PathVariable Integer id, HttpSession session) {
		Boolean deleted = voucherService.deleteVoucher(id);
		if (deleted) {
			session.setAttribute("succMsg", "Xóa mã giảm giá thành công");
		} else {
			session.setAttribute("errorMsg", "Không thể xóa mã giảm giá");
		}
		return "redirect:/admin/vouchers";
	}

	@GetMapping("/order/invoice/{orderId}")
	public org.springframework.http.ResponseEntity<byte[]> adminDownloadInvoice(@PathVariable String orderId) {
		List<ProductOrder> orders = productOrderRepository.findAllByOrderId(orderId);
		if (orders.isEmpty()) {
			ProductOrder single = productOrderRepository.findByOrderId(orderId);
			if (single != null) {
				orders = java.util.Collections.singletonList(single);
			}
		}
		if (orders.isEmpty()) {
			return org.springframework.http.ResponseEntity.notFound().build();
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

	@GetMapping("/chat")
	public String adminChat() {
		return "admin/chat";
	}

}
