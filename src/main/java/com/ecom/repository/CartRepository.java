package com.ecom.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecom.model.Cart;

public interface CartRepository extends JpaRepository<Cart, Integer> {

	public Cart findByProductIdAndUserId(Integer productId, Integer userId);

	@org.springframework.data.jpa.repository.Query("SELECT c FROM Cart c WHERE c.product.id = :productId AND c.user.id = :userId")
	public List<Cart> findCartsByProductIdAndUserId(@org.springframework.data.repository.query.Param("productId") Integer productId, @org.springframework.data.repository.query.Param("userId") Integer userId);

	public Integer countByUserId(Integer userId);

	public List<Cart> findByUserId(Integer userId);

}
