package com.ecom.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.ecom.model.Cart;
import com.ecom.model.Product;
import com.ecom.model.UserDtls;
import com.ecom.repository.CartRepository;
import com.ecom.repository.ProductRepository;
import com.ecom.repository.UserRepository;
import com.ecom.service.CartService;

@Service
public class CartServiceImpl implements CartService {

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ProductRepository productRepository;

	@Override
	public Cart getCartByProductAndUser(Integer productId, Integer userId) {
		List<Cart> carts = cartRepository.findCartsByProductIdAndUserId(productId, userId);
		if (carts == null || carts.isEmpty()) {
			return null;
		}
		if (carts.size() > 1) {
			// Merge duplicate rows and clean up database
			Cart primary = carts.get(0);
			int totalQty = (primary.getQuantity() != null) ? primary.getQuantity() : 0;
			for (int i = 1; i < carts.size(); i++) {
				if (carts.get(i).getQuantity() != null) {
					totalQty += carts.get(i).getQuantity();
				}
			}
			cartRepository.deleteAll(carts.subList(1, carts.size()));
			primary.setQuantity(totalQty);
			return cartRepository.save(primary);
		}
		return carts.get(0);
	}

	@Override
	public Cart saveCart(Integer productId, Integer userId) {
		return saveCart(productId, userId, 1);
	}

	@Override
	public Cart saveCart(Integer productId, Integer userId, Integer quantity) {
		if (quantity == null || quantity <= 0) {
			quantity = 1;
		}

		UserDtls userDtls = userRepository.findById(userId).orElse(null);
		Product product = productRepository.findById(productId).orElse(null);

		if (userDtls == null || product == null) {
			return null;
		}

		int availableStock = product.getStock();
		if (availableStock <= 0) {
			return null; // out of stock
		}

		Cart cart = getCartByProductAndUser(productId, userId);

		if (ObjectUtils.isEmpty(cart)) {
			int addQty = Math.min(quantity, availableStock);
			cart = new Cart();
			cart.setProduct(product);
			cart.setUser(userDtls);
			cart.setQuantity(addQty);
			cart.setTotalPrice(addQty * product.getDiscountPrice());
		} else {
			int currentQty = (cart.getQuantity() != null) ? cart.getQuantity() : 0;
			int newQty = currentQty + quantity;
			if (newQty > availableStock) {
				newQty = availableStock; // Strict warehouse stock ceiling
			}
			cart.setQuantity(newQty);
			cart.setTotalPrice(cart.getQuantity() * cart.getProduct().getDiscountPrice());
		}
		return cartRepository.save(cart);
	}

	@Override
	public List<Cart> getCartsByUser(Integer userId) {
		List<Cart> carts = cartRepository.findByUserId(userId);

		Double totalOrderPrice = 0.0;
		List<Cart> updateCarts = new ArrayList<>();
		for (Cart c : carts) {
			Double totalPrice = (c.getProduct().getDiscountPrice() * c.getQuantity());
			c.setTotalPrice(totalPrice);
			totalOrderPrice = totalOrderPrice + totalPrice;
			c.setTotalOrderPrice(totalOrderPrice);
			updateCarts.add(c);
		}

		return updateCarts;
	}

	@Override
	public Integer getCountCart(Integer userId) {
		Integer countByUserId = cartRepository.countByUserId(userId);
		return countByUserId;
	}

	@Override
	public void updateQuantity(String sy, Integer cid) {
		Cart cart = cartRepository.findById(cid).orElse(null);
		if (cart == null) return;

		int availableStock = (cart.getProduct() != null) 
				? cart.getProduct().getStock() : 999999;
		int updateQuantity;

		if (sy.equalsIgnoreCase("de")) {
			updateQuantity = cart.getQuantity() - 1;

			if (updateQuantity <= 0) {
				cartRepository.delete(cart);
			} else {
				cart.setQuantity(updateQuantity);
				cartRepository.save(cart);
			}
		} else {
			updateQuantity = cart.getQuantity() + 1;
			if (updateQuantity > availableStock) {
				updateQuantity = availableStock; // Stock ceiling
			}
			cart.setQuantity(updateQuantity);
			cartRepository.save(cart);
		}
	}

	@Override
	public void updateQuantityDirect(Integer cid, Integer quantity) {
		Cart cart = cartRepository.findById(cid).orElse(null);
		if (cart == null) return;

		int availableStock = (cart.getProduct() != null) 
				? cart.getProduct().getStock() : 999999;

		if (quantity == null || quantity <= 0) {
			cartRepository.delete(cart);
			return;
		}

		if (quantity > availableStock) {
			quantity = availableStock; // Clamp to available warehouse stock
		}

		cart.setQuantity(quantity);
		cartRepository.save(cart);
	}

}
