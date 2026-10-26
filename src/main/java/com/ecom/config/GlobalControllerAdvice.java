package com.ecom.config;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.ecom.controller.AdminController;
import com.ecom.controller.HomeController;
import com.ecom.controller.UserController;
import com.ecom.model.UserDtls;
import com.ecom.service.CartService;
import com.ecom.service.CategoryService;
import com.ecom.service.NotificationService;
import com.ecom.service.UserService;

/**
 * Cung cấp các thuộc tính dùng chung cho layout (navbar, badge giỏ hàng, thông báo, danh mục)
 * cho mọi Controller trả về view. Thay thế các hàm @ModelAttribute bị lặp lại trước đây.
 * Chỉ áp dụng cho các MVC Controller để tránh truy vấn thừa ở các REST API.
 */
@ControllerAdvice(assignableTypes = { HomeController.class, UserController.class, AdminController.class })
public class GlobalControllerAdvice {

	@Autowired
	private UserService userService;

	@Autowired
	private CartService cartService;

	@Autowired
	private NotificationService notificationService;

	@Autowired
	private CategoryService categoryService;

	@ModelAttribute
	public void addCommonAttributes(Principal p, Model m) {
		if (p != null) {
			UserDtls userDtls = userService.getUserByEmail(p.getName());
			if (userDtls != null) {
				m.addAttribute("user", userDtls);
				m.addAttribute("countCart", cartService.getCountCart(userDtls.getId()));
				Long countNotification = notificationService.getUnreadCount(userDtls.getId());
				m.addAttribute("countNotification", countNotification != null ? countNotification : 0L);
			}
		}
		m.addAttribute("categorys", categoryService.getAllActiveCategory());
	}
}
