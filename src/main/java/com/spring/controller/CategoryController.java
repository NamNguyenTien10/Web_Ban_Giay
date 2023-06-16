package com.spring.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.spring.entity.Category;
import com.spring.entity.Product;
import com.spring.repository.CategoryRepository;
import com.spring.repository.ProductRepository;

import jakarta.validation.Valid;

@Controller
public class CategoryController {

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ProductRepository productRepository;

	@GetMapping("/admin/category/list")
	public String findAllCategory(Model model, @RequestParam(name = "pageSize", defaultValue = "8") Integer pageSize,
			@RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum) {
		Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
		Page<Category> page = categoryRepository.findAll(pageable);

		model.addAttribute("totalPage", page.getTotalPages());
		model.addAttribute("list", page.getContent());
		return "template/list-category";
	}


	@PostMapping("/admin/category/add")
	public String addCategory(Model model, @Valid Category cate, BindingResult result) {
		if (result.hasErrors()) {
			return "template/add-category";
		} else {
			categoryRepository.save(cate);
			return "redirect:/admin/category/list";
		}
	}

	@GetMapping("/admin/category/view-add")
	public String getAddCategory(Model model) {
		model.addAttribute("category", new Category());
		return "template/add-category";
	}

	@GetMapping("/admin/category/detail/{id}")
	public String detailCategory(Model model, @PathVariable(name = "id") Integer productId) {
		model.addAttribute("detail", categoryRepository.findById(productId).orElse(null));
		return "template/detail-category";
	}

	@GetMapping("/admin/category/view-update/{id}")
	public String viewUpdateCategory(Model model, @PathVariable(name = "id") Integer productId) {
		model.addAttribute("detailPro", categoryRepository.findById(productId).orElse(null));
		return "template/update-category";
	}

	@PostMapping("/admin/category/update")
	public String updateCategory(Model model, Category cate) {
		categoryRepository.save(cate);
		return "redirect:/admin/category/list";
	}

	@GetMapping("/admin/category/delete/{id}")
	public String deleteCategory(@PathVariable(name = "id") Integer productId) {

		categoryRepository.deleteById(productId);
		return "redirect:/admin/category/list";
	}

	
	
	

	

}
