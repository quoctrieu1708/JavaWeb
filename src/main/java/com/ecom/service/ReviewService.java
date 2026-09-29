package com.ecom.service;

import java.util.List;
import java.util.Optional;

import com.ecom.model.Review;

public interface ReviewService {

	Review saveReview(Integer productId, Integer userId, Integer rating, String comment);

	List<Review> getReviewsByProduct(Integer productId);

	Long getReviewCountByProduct(Integer productId);

	Double getAverageRatingByProduct(Integer productId);

	Optional<Review> getUserReviewForProduct(Integer userId, Integer productId);
}
