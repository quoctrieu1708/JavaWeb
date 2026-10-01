package com.ecom.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecom.model.Product;
import com.ecom.service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductApiController {

	@Autowired
	private ProductService productService;

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
}
