package com.ecom.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ecom.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {

	List<Product> findByIsActiveTrue();

	Page<Product> findByIsActiveTrue(Pageable pageable);

	List<Product> findByCategory(String category);

	List<Product> findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(String ch, String ch2);

	Page<Product> findByCategory(Pageable pageable, String category);

	Page<Product> findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(String ch, String ch2,
			Pageable pageable);

	Page<Product> findByisActiveTrueAndTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(String ch, String ch2,
			Pageable pageable);

	@org.springframework.data.jpa.repository.Query("SELECT p FROM Product p WHERE p.isActive = true " +
			"AND (:category IS NULL OR :category = '' OR p.category = :category) " +
			"AND (:keyword IS NULL OR :keyword = '' OR LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(p.category) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
			"AND (:minPrice IS NULL OR p.discountPrice >= :minPrice) " +
			"AND (:maxPrice IS NULL OR p.discountPrice <= :maxPrice)")
	Page<Product> searchAndFilterProducts(
			@org.springframework.data.repository.query.Param("category") String category,
			@org.springframework.data.repository.query.Param("keyword") String keyword,
			@org.springframework.data.repository.query.Param("minPrice") Double minPrice,
			@org.springframework.data.repository.query.Param("maxPrice") Double maxPrice,
			Pageable pageable);

	Long countByStockLessThan(int threshold);

	Long countByStockEquals(int stock);
}
