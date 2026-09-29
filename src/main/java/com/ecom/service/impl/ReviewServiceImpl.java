package com.ecom.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecom.model.Product;
import com.ecom.model.Review;
import com.ecom.model.UserDtls;
import com.ecom.repository.ProductRepository;
import com.ecom.repository.ReviewRepository;
import com.ecom.repository.UserRepository;
import com.ecom.service.ReviewService;

@Service
public class ReviewServiceImpl implements ReviewService {

	@Autowired
	private ReviewRepository reviewRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private UserRepository userRepository;

	@Override
	@Transactional
	public Review saveReview(Integer productId, Integer userId, Integer rating, String comment) {
		Product product = productRepository.findById(productId).orElse(null);
		UserDtls user = userRepository.findById(userId).orElse(null);

		if (product == null || user == null) {
			return null;
		}

		Review review = reviewRepository.findByUserIdAndProductId(userId, productId)
				.orElse(new Review());

		review.setProduct(product);
		review.setUser(user);
		review.setRating(rating != null ? Math.max(1, Math.min(5, rating)) : 5);
		review.setComment(comment);
		review.setCreatedAt(LocalDateTime.now());

		Review savedReview = reviewRepository.save(review);

		// Cập nhật rating trung bình và reviewCount trên Product
		Double avg = reviewRepository.getAverageRatingByProductId(productId);
		Long count = reviewRepository.countByProductId(productId);

		product.setRating(avg != null ? Math.round(avg * 10.0) / 10.0 : 5.0);
		product.setReviewCount(count != null ? count.intValue() : 0);
		productRepository.save(product);

		return savedReview;
	}

	@Override
	public List<Review> getReviewsByProduct(Integer productId) {
		return reviewRepository.findByProductIdOrderByCreatedAtDesc(productId);
	}

	@Override
	public Long getReviewCountByProduct(Integer productId) {
		return reviewRepository.countByProductId(productId);
	}

	@Override
	public Double getAverageRatingByProduct(Integer productId) {
		Double avg = reviewRepository.getAverageRatingByProductId(productId);
		return avg != null ? Math.round(avg * 10.0) / 10.0 : 5.0;
	}

	@Override
	public Optional<Review> getUserReviewForProduct(Integer userId, Integer productId) {
		return reviewRepository.findByUserIdAndProductId(userId, productId);
	}
}
