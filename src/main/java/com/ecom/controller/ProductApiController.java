package com.ecom.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecom.model.Product;
import com.ecom.service.ProductSeedService;
import com.ecom.service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductApiController {

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductSeedService productSeedService;

	@GetMapping("/search")
	public List<Map<String, Object>> searchProductsAjax(@RequestParam(value = "q", defaultValue = "") String query) {
		String trimmed = query.trim();
		if (trimmed.length() < 1) {
			return Collections.emptyList();
		}

		List<Product> products = productService.searchProduct(trimmed);
		if (products == null) {
			return Collections.emptyList();
		}

		return products.stream()
				.filter(p -> Boolean.TRUE.equals(p.getIsActive()))
				.limit(10) // fetch up to 10 matching products
				.map(p -> {
					Map<String, Object> map = new HashMap<>();
					map.put("id", p.getId());
					map.put("title", p.getTitle());
					map.put("category", p.getCategory());
					map.put("image", p.getImage());
					map.put("price", p.getPrice());
					map.put("discountPrice", p.getDiscountPrice());
					map.put("discount", p.getDiscount());
					map.put("stock", p.getStock());
					return map;
				})
				.collect(Collectors.toList());
	}

	/**
	 * Thêm 1 sản phẩm đơn lẻ (hỗ trợ Postman Runner / Single Request).
	 */
	@PostMapping
	public ResponseEntity<?> createProduct(@RequestBody Product product) {
		if (product.getTitle() == null || product.getTitle().trim().isEmpty()) {
			return ResponseEntity.badRequest().body(Map.of("error", "Tiêu đề sản phẩm không được để trống"));
		}
		if (product.getPrice() == null || product.getPrice() < 0) {
			return ResponseEntity.badRequest().body(Map.of("error", "Giá sản phẩm không hợp lệ"));
		}
		prepareProductDefaults(product);
		Product saved = productService.saveProduct(product);
		return ResponseEntity.status(HttpStatus.CREATED).body(saved);
	}

	/**
	 * Xóa sản phẩm theo ID (hỗ trợ Postman).
	 */
	@org.springframework.web.bind.annotation.DeleteMapping("/{id}")
	public ResponseEntity<?> deleteProduct(@org.springframework.web.bind.annotation.PathVariable("id") Integer id) {
		Boolean deleted = productService.deleteProduct(id);
		if (Boolean.TRUE.equals(deleted)) {
			return ResponseEntity.ok(Map.of("status", "success", "message", "Đã xóa sản phẩm ID " + id));
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Không tìm thấy sản phẩm ID " + id));
	}

	/**
	 * Thêm nhiều sản phẩm cùng lúc từ JSON array (Bulk Import trong Postman).
	 */
	@PostMapping("/bulk")
	public ResponseEntity<?> createProductsBulk(@RequestBody List<Product> products) {
		if (products == null || products.isEmpty()) {
			return ResponseEntity.badRequest().body(Map.of("error", "Danh sách sản phẩm trống"));
		}
		List<Product> savedList = new ArrayList<>();
		for (Product p : products) {
			if (p.getTitle() == null || p.getTitle().trim().isEmpty()) continue;
			prepareProductDefaults(p);
			savedList.add(productService.saveProduct(p));
		}
		return ResponseEntity.ok(Map.of(
			"status", "success",
			"insertedCount", savedList.size(),
			"message", "Đã thêm thành công " + savedList.size() + " sản phẩm vào hệ thống"
		));
	}

	/**
	 * Nạp ngay 100 sản phẩm mẫu chuẩn 8 danh mục Figma 2.0 (One-Click Seeder).
	 * Hỗ trợ cả POST và GET để test nhanh trực tiếp qua trình duyệt hoặc Postman.
	 */
	@PostMapping("/seed-100")
	public ResponseEntity<?> seed100ProductsPost(@RequestParam(value = "reset", defaultValue = "false") boolean reset) {
		Map<String, Object> res = productSeedService.seed100Products(reset);
		return ResponseEntity.ok(res);
	}

	@GetMapping("/seed-100")
	public ResponseEntity<?> seed100ProductsGet(@RequestParam(value = "reset", defaultValue = "false") boolean reset) {
		Map<String, Object> res = productSeedService.seed100Products(reset);
		return ResponseEntity.ok(res);
	}

	/**
	 * Xuất danh sách 100 sản phẩm mẫu dạng JSON để import vào Postman hoặc lưu file.
	 */
	@GetMapping("/export-100")
	public ResponseEntity<List<Product>> export100Products() {
		return ResponseEntity.ok(productSeedService.generate100Products());
	}

	/**
	 * Báo cáo thống kê số lượng sản phẩm chia theo danh mục trong hệ thống.
	 */
	@GetMapping("/summary")
	public ResponseEntity<?> getProductsSummary() {
		List<Product> products = productService.getAllProducts();
		Map<String, Long> countByCategory = products.stream()
				.collect(Collectors.groupingBy(p -> p.getCategory() == null ? "Chưa phân loại" : p.getCategory(), Collectors.counting()));
		Map<String, Object> response = new LinkedHashMap<>();
		response.put("totalProducts", products.size());
		response.put("byCategory", countByCategory);
		return ResponseEntity.ok(response);
	}

	private void prepareProductDefaults(Product product) {
		if (product.getImage() == null || product.getImage().trim().isEmpty()) {
			product.setImage("default.jpg");
		}
		if (product.getDiscount() < 0 || product.getDiscount() > 100) {
			product.setDiscount(0);
		}
		if (product.getDiscount() > 0 && product.getPrice() != null) {
			double dp = product.getPrice() - (product.getPrice() * product.getDiscount() / 100.0);
			product.setDiscountPrice(Math.round(dp * 100.0) / 100.0);
		} else {
			product.setDiscountPrice(product.getPrice());
		}
		if (product.getIsActive() == null) {
			product.setIsActive(true);
		}
		if (product.getRating() == null) {
			product.setRating(5.0);
		}
		if (product.getReviewCount() == null) {
			product.setReviewCount(0);
		}
	}
}
