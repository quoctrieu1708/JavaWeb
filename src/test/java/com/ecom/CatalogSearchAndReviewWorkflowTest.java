package com.ecom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import com.ecom.controller.ProductApiController;
import com.ecom.model.Category;
import com.ecom.model.Product;
import com.ecom.model.Review;
import com.ecom.model.UserDtls;
import com.ecom.service.CategoryService;
import com.ecom.service.ProductService;
import com.ecom.service.ReviewService;
import com.ecom.service.UserService;

/**
 * WORKFLOW TEST NHÓM 2: Danh mục, Sản phẩm, Bộ lọc nâng cao, Live Search API & Đánh giá sao.
 * Thay thế thao tác thủ công cho các kịch bản HOME-01 -> HOME-04, CAT-01 -> CAT-04, PRD-01 -> PRD-04, ADM-02 -> ADM-03.
 */
@SpringBootTest
@Transactional
class CatalogSearchAndReviewWorkflowTest {

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private ProductService productService;

	@Autowired
	private ReviewService reviewService;

	@Autowired
	private UserService userService;

	@Autowired
	private ProductApiController productApiController;

	private Product createTestProduct(String title, String categoryName, double price, int discount, int stock) {
		Product p = new Product();
		p.setTitle(title);
		p.setDescription("Sản phẩm kiểm thử tự động chất lượng cao");
		p.setCategory(categoryName);
		p.setPrice(price);
		p.setDiscount(discount);
		p.setDiscountPrice(price - (price * discount / 100.0));
		p.setStock(stock);
		p.setIsActive(true);
		p.setImage("default_product.png");
		return productService.saveProduct(p);
	}

	@Test
	@DisplayName("ADM-02 & ADM-03: Thêm Danh mục, Thêm Sản phẩm và Tự động tính giá sau giảm (discountPrice)")
	void testCategoryAndProductLifecycleWithDiscountCalculation() {
		String uniqueCatName = "Thiết bị VIP " + UUID.randomUUID().toString().substring(0, 5);
		Category cat = new Category();
		cat.setName(uniqueCatName);
		cat.setIsActive(true);
		cat.setImageName("cat_test.png");

		Category savedCat = categoryService.saveCategory(cat);
		assertNotNull(savedCat.getId());
		assertTrue(categoryService.existCategory(uniqueCatName));

		Product savedProd = createTestProduct("MacBook Pro M4 Luxury", uniqueCatName, 50_000_000.0, 0, 20);
		assertNotNull(savedProd.getId());

		// Cập nhật giảm giá 15%
		savedProd.setDiscount(15);
		Product updatedProd = productService.updateProduct(savedProd, null);
		assertEquals(42_500_000.0, updatedProd.getDiscountPrice(), 0.01, "Giá bán thực tế sau giảm 15% của 50tr phải là 42.5tr");
	}

	@Test
	@DisplayName("HOME-01 & CAT-02: Truy vấn Top 8 sản phẩm mới nhất và Lọc kết hợp (Danh mục + Khoảng giá + Sắp xếp)")
	void testHomepageLimitAndAdvancedCatalogFiltering() {
		String catName = "Đồng hồ thông minh " + UUID.randomUUID().toString().substring(0, 4);
		createTestProduct("Apple Watch Ultra Gold", catName, 25_000_000.0, 10, 15); // 22.5tr
		createTestProduct("Garmin Fenix Sapphire", catName, 30_000_000.0, 0, 10);   // 30.0tr
		createTestProduct("Xiaomi Band Basic", catName, 1_000_000.0, 0, 50);       // 1.0tr

		// Kiểm tra trang chủ lấy tối đa 8 sản phẩm
		List<Product> latest8 = productService.getLatestActiveProducts(8);
		assertFalse(latest8.isEmpty());
		assertTrue(latest8.size() <= 8, "Trang chủ chỉ được phép lấy tối đa 8 sản phẩm mới nhất");

		// Lọc trong khoảng giá [15tr, 35tr] và sắp xếp giá tăng dần (price_asc)
		Page<Product> filteredPage = productService.filterProducts(catName, null, 15_000_000.0, 35_000_000.0, "price_asc", 0, 12);
		List<Product> items = filteredPage.getContent();
		assertEquals(2, items.size(), "Chỉ có 2 sản phẩm nằm trong khoảng giá 15tr - 35tr");
		assertEquals("Apple Watch Ultra Gold", items.get(0).getTitle(), "Sản phẩm giá 22.5tr phải đứng trước sản phẩm 30tr khi sort price_asc");
		assertEquals("Garmin Fenix Sapphire", items.get(1).getTitle());
	}

	@Test
	@DisplayName("HOME-03: Kiểm tra REST API Gợi ý Tìm kiếm Trực tiếp (Live Search Autocomplete)")
	void testLiveSearchAutocompleteApi() {
		String uniqueKeyword = "VertuSignature" + UUID.randomUUID().toString().substring(0, 4);
		createTestProduct(uniqueKeyword + " Gold Edition", "Điện thoại", 120_000_000.0, 5, 3);

		List<Map<String, Object>> suggestions = productApiController.searchProductsAjax(uniqueKeyword);
		assertNotNull(suggestions);
		assertFalse(suggestions.isEmpty(), "API Live Search phải trả về gợi ý khớp từ khóa");
		assertTrue(suggestions.get(0).get("title").toString().contains(uniqueKeyword));
	}

	@Test
	@DisplayName("PRD-04: Gửi đánh giá sao (Review) và tự động tính lại điểm trung bình (avgRating) của sản phẩm")
	void testProductReviewAndRatingCalculation() {
		Product product = createTestProduct("Tai nghe Bang & Olufsen H95", "Âm thanh", 22_000_000.0, 0, 8);

		UserDtls u1 = new UserDtls();
		u1.setName("Reviewer 1");
		u1.setEmail("rev1_" + UUID.randomUUID().toString().substring(0, 5) + "@test.com");
		u1.setPassword("123456");
		u1 = userService.saveUser(u1);

		UserDtls u2 = new UserDtls();
		u2.setName("Reviewer 2");
		u2.setEmail("rev2_" + UUID.randomUUID().toString().substring(0, 5) + "@test.com");
		u2.setPassword("123456");
		u2 = userService.saveUser(u2);

		// User 1 đánh giá 5 sao, User 2 đánh giá 4 sao
		reviewService.saveReview(product.getId(), u1.getId(), 5, "Âm thanh tuyệt đỉnh!");
		reviewService.saveReview(product.getId(), u2.getId(), 4, "Thiết kế sang trọng, pin tốt.");

		List<Review> reviews = reviewService.getReviewsByProduct(product.getId());
		assertEquals(2, reviews.size());

		Product updatedProduct = productService.getProductById(product.getId());
		assertEquals(4.5, updatedProduct.getRating(), 0.01, "Điểm trung bình của 5 sao và 4 sao phải là 4.5");
		assertEquals(2, updatedProduct.getReviewCount());
	}
}
