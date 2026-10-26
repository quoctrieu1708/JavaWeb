package com.ecom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.ecom.model.Cart;
import com.ecom.model.Product;
import com.ecom.model.UserDtls;
import com.ecom.repository.CartRepository;
import com.ecom.service.CartService;
import com.ecom.service.ProductService;
import com.ecom.service.UserService;

/**
 * WORKFLOW TEST NHÓM 3: Giỏ hàng thông minh, Cộng dồn sản phẩm trùng & Chặn vượt quá tồn kho.
 * Thay thế thao tác thủ công cho các kịch bản CART-01 -> CART-05.
 */
@SpringBootTest
@Transactional
class CartAndStockConstraintWorkflowTest {

	@Autowired
	private CartService cartService;

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private ProductService productService;

	@Autowired
	private UserService userService;

	private UserDtls createTestUser() {
		UserDtls u = new UserDtls();
		u.setName("Khách Hàng Giỏ Hàng");
		u.setEmail("cart_" + UUID.randomUUID().toString().substring(0, 6) + "@test.com");
		u.setPassword("123456");
		return userService.saveUser(u);
	}

	private Product createTestProduct(int stock) {
		Product p = new Product();
		p.setTitle("iPhone 16 Pro Max Titanium " + UUID.randomUUID().toString().substring(0, 4));
		p.setCategory("Điện thoại");
		p.setPrice(35_000_000.0);
		p.setDiscount(10);
		p.setDiscountPrice(31_500_000.0);
		p.setStock(stock);
		p.setIsActive(true);
		p.setImage("iphone.png");
		return productService.saveProduct(p);
	}

	@Test
	@DisplayName("CART-01 & CART-02: Thêm mới vào giỏ và Cộng dồn số lượng khi trùng sản phẩm (không tạo dòng trùng lặp)")
	void testAddNewAndMergeDuplicateProductInCart() {
		UserDtls user = createTestUser();
		Product product = createTestProduct(10);

		// Lần 1: Thêm 2 sản phẩm vào giỏ
		Cart firstAdd = cartService.saveCart(product.getId(), user.getId(), 2);
		assertNotNull(firstAdd);
		assertEquals(2, firstAdd.getQuantity());

		// Lần 2: Thêm tiếp 3 sản phẩm cùng loại vào giỏ -> Phải cộng dồn thành 5 trên cùng 1 dòng
		Cart secondAdd = cartService.saveCart(product.getId(), user.getId(), 3);
		assertNotNull(secondAdd);
		assertEquals(firstAdd.getId(), secondAdd.getId(), "Phải cập nhật trên cùng bản ghi Cart, không tạo bản ghi mới");
		assertEquals(5, secondAdd.getQuantity(), "Số lượng phải được cộng dồn từ 2 + 3 = 5");

		List<Cart> userCarts = cartRepository.findCartsByProductIdAndUserId(product.getId(), user.getId());
		assertEquals(1, userCarts.size(), "Trong DB chỉ được phép có duy nhất 1 dòng cho cặp (productId, userId)");
	}

	@Test
	@DisplayName("CART-03: Tự động giới hạn số lượng ở mức tồn kho tối đa (stock cap) khi khách thêm vượt kho")
	void testAddToCartCapsAtAvailableStock() {
		UserDtls user = createTestUser();
		Product product = createTestProduct(6); // Kho chỉ có 6 chiếc

		// Thêm 4 chiếc trước
		cartService.saveCart(product.getId(), user.getId(), 4);

		// Cố tình thêm tiếp 10 chiếc nữa -> Tổng không được vượt quá 6
		Cart cappedCart = cartService.saveCart(product.getId(), user.getId(), 10);
		assertEquals(6, cappedCart.getQuantity(), "Số lượng trong giỏ phải bị chặn ở mức tồn kho tối đa là 6");
	}

	@Test
	@DisplayName("CART-04 & CART-05: Tăng/Giảm số lượng (+ / -), Nhập số lượng trực tiếp và Xóa khi giảm về 0")
	void testUpdateQuantityButtonsAndDirectInput() {
		UserDtls user = createTestUser();
		Product product = createTestProduct(8);

		Cart cart = cartService.saveCart(product.getId(), user.getId(), 2);

		// Nhấn nút '+' (sy = "in") -> tăng từ 2 lên 3
		cartService.updateQuantity("in", cart.getId());
		Cart afterIncrease = cartService.getCartByProductAndUser(product.getId(), user.getId());
		assertEquals(3, afterIncrease.getQuantity());

		// Nhập trực tiếp số lượng 7
		cartService.updateQuantityDirect(cart.getId(), 7);
		Cart afterDirect = cartService.getCartByProductAndUser(product.getId(), user.getId());
		assertEquals(7, afterDirect.getQuantity());

		// Nhập trực tiếp số 50 (vượt kho 8) -> Tự động đưa về 8
		cartService.updateQuantityDirect(cart.getId(), 50);
		Cart afterOverStock = cartService.getCartByProductAndUser(product.getId(), user.getId());
		assertEquals(8, afterOverStock.getQuantity(), "Nhập trực tiếp vượt tồn kho phải tự động giới hạn về 8");

		// Đưa về 1 rồi nhấn nút '-' (sy = "de") -> Xóa khỏi giỏ hàng
		cartService.updateQuantityDirect(cart.getId(), 1);
		cartService.updateQuantity("de", cart.getId());
		List<Cart> remaining = cartService.getCartsByUser(user.getId());
		assertEquals(0, remaining.size(), "Khi giảm từ 1 xuống 0 thì sản phẩm phải được xóa khỏi giỏ hàng");
	}
}
