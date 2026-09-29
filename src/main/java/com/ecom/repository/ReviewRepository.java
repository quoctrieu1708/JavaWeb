package com.ecom.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ecom.model.Review;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

	List<Review> findByProductIdOrderByCreatedAtDesc(Integer productId);

	Long countByProductId(Integer productId);

	Optional<Review> findByUserIdAndProductId(Integer userId, Integer productId);

	@Query("SELECT AVG(r.rating) FROM Review r WHERE r.product.id = :productId")
	Double getAverageRatingByProductId(@Param("productId") Integer productId);
}
