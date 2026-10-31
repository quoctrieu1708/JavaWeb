package com.ecom.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.ecom.model.Product;
import com.ecom.repository.ProductRepository;
import com.ecom.service.FileStorageService;
import com.ecom.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private FileStorageService fileStorageService;

	@Override
	public Product saveProduct(Product product) {
		return productRepository.save(product);
	}

	@Override
	public List<Product> getAllProducts() {
		return productRepository.findAll();
	}

	@Override
	public Page<Product> getAllProductsPagination(Integer pageNo, Integer pageSize) {
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		return productRepository.findAll(pageable);
	}

	@Override
	public Boolean deleteProduct(Integer id) {
		Product product = productRepository.findById(id).orElse(null);

		if (!ObjectUtils.isEmpty(product)) {
			productRepository.delete(product);
			return true;
		}
		return false;
	}

	@Override
	public Product getProductById(Integer id) {
		Product product = productRepository.findById(id).orElse(null);
		return product;
	}

	@Override
	public Product updateProduct(Product product, MultipartFile image) {

		Product dbProduct = getProductById(product.getId());

		// Lưu ảnh mới (nếu có) vào thư mục upload ngoài, an toàn khi chạy dưới dạng JAR
		String savedName = null;
		try {
			savedName = fileStorageService.saveFile(image, "product_img");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String imageName = savedName == null ? dbProduct.getImage() : savedName;

		dbProduct.setTitle(product.getTitle());
		dbProduct.setDescription(product.getDescription());
		dbProduct.setCategory(product.getCategory());
		dbProduct.setPrice(product.getPrice());
		dbProduct.setStock(product.getStock());
		dbProduct.setImage(imageName);
		dbProduct.setIsActive(product.getIsActive());
		dbProduct.setDiscount(product.getDiscount());

		// 5=100*(5/100); 100-5=95
		Double disocunt = product.getPrice() * (product.getDiscount() / 100.0);
		Double discountPrice = product.getPrice() - disocunt;
		dbProduct.setDiscountPrice(discountPrice);

		Product updateProduct = productRepository.save(dbProduct);

		if (!ObjectUtils.isEmpty(updateProduct)) {
			return product;
		}
		return null;
	}

	@Override
	public List<Product> getAllActiveProducts(String category) {
		List<Product> products = null;
		if (ObjectUtils.isEmpty(category)) {
			products = productRepository.findByIsActiveTrue();
		} else {
			products = productRepository.findByCategory(category);
		}

		return products;
	}

	@Override
	public List<Product> searchProduct(String ch) {
		return productRepository.findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(ch, ch);
	}

	@Override
	public Page<Product> searchProductPagination(Integer pageNo, Integer pageSize, String ch) {
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		return productRepository.findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(ch, ch, pageable);
	}

	@Override
	public Page<Product> getAllActiveProductPagination(Integer pageNo, Integer pageSize, String category) {

		Pageable pageable = PageRequest.of(pageNo, pageSize);
		Page<Product> pageProduct = null;

		if (ObjectUtils.isEmpty(category)) {
			pageProduct = productRepository.findByIsActiveTrue(pageable);
		} else {
			pageProduct = productRepository.findByCategory(pageable, category);
		}
		return pageProduct;
	}

	@Override
	public Page<Product> searchActiveProductPagination(Integer pageNo, Integer pageSize, String category, String ch) {

		Page<Product> pageProduct = null;
		Pageable pageable = PageRequest.of(pageNo, pageSize);

		pageProduct = productRepository.findByisActiveTrueAndTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(ch,
				ch, pageable);

//		if (ObjectUtils.isEmpty(category)) {
//			pageProduct = productRepository.findByIsActiveTrue(pageable);
//		} else {
//			pageProduct = productRepository.findByCategory(pageable, category);
//		}
		return pageProduct;
	}

	@Override
	public Page<Product> filterProducts(String category, String keyword, Double minPrice, Double maxPrice, String sortBy, Integer pageNo, Integer pageSize) {
		org.springframework.data.domain.Sort sort = org.springframework.data.domain.Sort.by("id").descending();
		if ("price_asc".equalsIgnoreCase(sortBy)) {
			sort = org.springframework.data.domain.Sort.by("discountPrice").ascending();
		} else if ("price_desc".equalsIgnoreCase(sortBy)) {
			sort = org.springframework.data.domain.Sort.by("discountPrice").descending();
		} else if ("rating".equalsIgnoreCase(sortBy)) {
			sort = org.springframework.data.domain.Sort.by("rating").descending();
		} else if ("latest".equalsIgnoreCase(sortBy)) {
			sort = org.springframework.data.domain.Sort.by("id").descending();
		}

		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
		String cat = (category != null && !category.trim().isEmpty() && !"all".equalsIgnoreCase(category.trim())) ? category.trim() : null;
		String kw = (keyword != null && !keyword.trim().isEmpty()) ? keyword.trim() : null;

		return productRepository.searchAndFilterProducts(cat, kw, minPrice, maxPrice, pageable);
	}

	@Override
	public Long countLowStock(int threshold) {
		return productRepository.countByStockLessThan(threshold);
	}

	@Override
	public Long countOutOfStock() {
		return productRepository.countByStockEquals(0);
	}

	@Override
	public Long countProducts() {
		return productRepository.count();
	}

	@Override
	public List<Product> getLatestActiveProducts(int limit) {
		Pageable pageable = PageRequest.of(0, limit, org.springframework.data.domain.Sort.by("id").descending());
		return productRepository.findByIsActiveTrue(pageable).getContent();
	}

	@Override
	public List<Product> getFlashSaleProducts(int limit) {
		Pageable pageable = PageRequest.of(0, limit);
		List<Product> flashSale = productRepository.findFlashSaleProducts(pageable);
		if (flashSale == null || flashSale.isEmpty()) {
			return getLatestActiveProducts(limit);
		}
		return flashSale;
	}

}
