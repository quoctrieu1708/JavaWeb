package com.ecom.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.ecom.model.Category;
import com.ecom.repository.CategoryRepository;
import com.ecom.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	public Category saveCategory(Category category) {
		return categoryRepository.save(category);
	}

	@Override
	public List<Category> getAllCategory() {
		return categoryRepository.findAll();
	}

	@Override
	public Boolean existCategory(String name) {
		return categoryRepository.existsByName(name);
	}

	@Override
	public Boolean deleteCategory(int id) {
		Category category = categoryRepository.findById(id).orElse(null);

		if (!ObjectUtils.isEmpty(category)) {
			categoryRepository.delete(category);
			return true;
		}
		return false;
	}

	@Override
	public Category getCategoryById(int id) {
		Category category = categoryRepository.findById(id).orElse(null);
		return category;
	}

	private static final List<String> FIGMA_CATEGORY_ORDER = List.of(
			"Điện thoại", "Laptop", "Màn hình", "Gia dụng",
			"Thời trang", "Làm đẹp", "Sách", "Thực phẩm"
	);

	@Override
	public List<Category> getAllActiveCategory() {
		List<Category> categories = new java.util.ArrayList<>(categoryRepository.findByIsActiveTrue());
		categories.sort((c1, c2) -> {
			int idx1 = FIGMA_CATEGORY_ORDER.indexOf(c1.getName());
			int idx2 = FIGMA_CATEGORY_ORDER.indexOf(c2.getName());
			if (idx1 != -1 && idx2 != -1) {
				return Integer.compare(idx1, idx2);
			} else if (idx1 != -1) {
				return -1;
			} else if (idx2 != -1) {
				return 1;
			}
			return Integer.compare(c1.getId(), c2.getId());
		});
		return categories;
	}

	@Override
	public Page<Category> getAllCategorPagination(Integer pageNo, Integer pageSize) {
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		return categoryRepository.findAll(pageable);
	}

}
