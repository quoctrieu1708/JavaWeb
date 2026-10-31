package com.ecom.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ecom.model.Category;
import com.ecom.model.Product;
import com.ecom.repository.CategoryRepository;
import com.ecom.repository.ProductRepository;

/**
 * Tự động đồng bộ các danh mục sản phẩm trong Database theo đúng đặc tả Figma Design 2.0:
 * 1. Điện thoại (mobile.png)
 * 2. Laptop (laptop.jpg)
 * 3. Màn hình (monitor.jpg)
 * 4. Gia dụng (appli.png)
 * 5. Thời trang (blue shirt.jfif)
 * 6. Làm đẹp (beuty.png)
 * 7. Sách (book.jpg)
 * 8. Thực phẩm (groccery.jpg)
 */
@Component
@Order(10)
public class DatabaseCategoryInitializer implements CommandLineRunner {

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ProductRepository productRepository;

	@Override
	@Transactional
	public void run(String... args) throws Exception {
		// 1. Chuyển đổi tên danh mục cũ sang tên mới chuẩn Figma 2.0
		renameCategoryIfExists("Điện thoại & Tablet", "Điện thoại", "mobile.png");
		renameCategoryIfExists("Laptop & Máy tính", "Laptop", "laptop.jpg");
		renameCategoryIfExists("Đồ gia dụng", "Gia dụng", "appli.png");
		renameCategoryIfExists("Thời trang nam nữ", "Thời trang", "blue shirt.jfif");
		renameCategoryIfExists("Mỹ phẩm & Làm đẹp", "Làm đẹp", "beuty.png");
		renameCategoryIfExists("Sách & VPP", "Sách", "book.jpg");
		deactivateCategoryIfExists("Phụ kiện công nghệ");
		deactivateCategoryIfExists("Phụ kiện");

		// 2. Đảm bảo toàn bộ 8 danh mục chuẩn của Figma đều tồn tại và kích hoạt
		ensureCategory("Điện thoại", "mobile.png");
		ensureCategory("Laptop", "laptop.jpg");
		ensureCategory("Màn hình", "monitor.jpg");
		ensureCategory("Gia dụng", "appli.png");
		ensureCategory("Thời trang", "blue shirt.jfif");
		ensureCategory("Làm đẹp", "beuty.png");
		ensureCategory("Sách", "book.jpg");
		ensureCategory("Thực phẩm", "groccery.jpg");

		// 3. Đồng bộ danh mục của sản phẩm trong bảng product
		productRepository.updateProductCategory("Điện thoại & Tablet", "Điện thoại");
		productRepository.updateProductCategory("Laptop & Máy tính", "Laptop");
		productRepository.updateProductCategory("Đồ gia dụng", "Gia dụng");
		productRepository.updateProductCategory("Thời trang nam nữ", "Thời trang");
		productRepository.updateProductCategory("Mỹ phẩm & Làm đẹp", "Làm đẹp");
		productRepository.updateProductCategory("Sách & VPP", "Sách");
		productRepository.updateProductCategory("Phụ kiện công nghệ", "Phụ kiện");

		// 4. Chuyển riêng các sản phẩm màn hình sang đúng danh mục "Màn hình"
		List<Product> products = productRepository.findAll();
		if (products != null) {
			for (Product p : products) {
				if (p.getTitle() != null && p.getTitle().toLowerCase().contains("màn hình")) {
					if (!"Màn hình".equals(p.getCategory())) {
						p.setCategory("Màn hình");
						productRepository.save(p);
					}
				}
			}
		}
	}

	private void renameCategoryIfExists(String oldName, String newName, String newImage) {
		Category cat = categoryRepository.findByName(oldName);
		if (cat != null) {
			Category target = categoryRepository.findByName(newName);
			if (target != null && !target.getId().equals(cat.getId())) {
				cat.setIsActive(false);
				categoryRepository.save(cat);
			} else {
				cat.setName(newName);
				if (newImage != null) {
					cat.setImageName(newImage);
				}
				cat.setIsActive(true);
				categoryRepository.save(cat);
			}
		}
	}

	private void deactivateCategoryIfExists(String name) {
		Category cat = categoryRepository.findByName(name);
		if (cat != null && Boolean.TRUE.equals(cat.getIsActive())) {
			cat.setIsActive(false);
			categoryRepository.save(cat);
		}
	}

	private void ensureCategory(String name, String image) {
		Category cat = categoryRepository.findByName(name);
		if (cat == null) {
			Category newCat = new Category();
			newCat.setName(name);
			newCat.setImageName(image);
			newCat.setIsActive(true);
			categoryRepository.save(newCat);
		} else {
			boolean changed = false;
			if (!Boolean.TRUE.equals(cat.getIsActive())) {
				cat.setIsActive(true);
				changed = true;
			}
			if (image != null && !image.equals(cat.getImageName())) {
				cat.setImageName(image);
				changed = true;
			}
			if (changed) {
				categoryRepository.save(cat);
			}
		}
	}
}
