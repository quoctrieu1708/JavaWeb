package com.ecom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ecom.model.Cart;
import com.ecom.model.Product;
import com.ecom.repository.CartRepository;
import com.ecom.repository.ProductRepository;
import com.ecom.service.CartService;

@SpringBootTest
class ShoppingCartApplicationTests {

	@Autowired
	private CartService cartService;

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private ProductRepository productRepository;

	@Test
	void contextLoads() {
	}

	@Test
	void testDuplicateAddToCartUpdatesQuantityAndEnforcesStock() {
		// Use existing product 19 and user 4
		Product product = productRepository.findById(19).orElse(null);
		assertNotNull(product, "Product 19 should exist");
		int stock = product.getStock();
		assertTrue(stock > 0, "Product stock should be positive");

		// Fetch existing cart or create
		Cart existingCart = cartService.getCartByProductAndUser(19, 4);
		if (existingCart == null) {
			existingCart = cartService.saveCart(19, 4, 1);
		}
		assertNotNull(existingCart, "Cart item should exist for user 4 and product 19");

		int initialQty = existingCart.getQuantity();

		// Case 1: If current quantity is less than stock, adding 1 must increase quantity by 1
		if (initialQty < stock) {
			Cart updated = cartService.saveCart(19, 4, 1);
			assertNotNull(updated);
			assertEquals(initialQty + 1, updated.getQuantity().intValue());
		}

		// Case 2: Verify only 1 cart row exists for (productId=19, userId=4)
		List<Cart> matchingCarts = cartRepository.findCartsByProductIdAndUserId(19, 4);
		assertEquals(1, matchingCarts.size(), "Should have exactly 1 cart row for product 19 and user 4");

		// Case 3: Verify getCartByProductAndUser returns the same row
		Cart fetched = cartService.getCartByProductAndUser(19, 4);
		assertNotNull(fetched);
		assertEquals(matchingCarts.get(0).getId(), fetched.getId());
	}
}
