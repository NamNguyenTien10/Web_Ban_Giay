package com.spring.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.spring.entity.Accounts;
import com.spring.entity.Cart;
import com.spring.entity.Category;
import com.spring.entity.Product;
import com.spring.repository.CartItemRepository;
import com.spring.repository.CartRepository;
import com.spring.repository.CategoryRepository;
import com.spring.repository.ProductRepository;
import com.spring.repository.SizeRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
	static List<Product> product = new ArrayList<>();
	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private SizeRepository sizeRepository;

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private CartItemRepository cartItemRepository;

	@Autowired
	private CartItemController cartItemController;

	@GetMapping("/home")
	public String homePage(Model model, @RequestParam(name = "pageSize", defaultValue = "8") Integer pageSize,
			@RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
			HttpSession session) {

		Accounts account = (Accounts) session.getAttribute("userLogged");
		if (account != null) {
			Cart cart = cartRepository.findByAccount(account);
			model.addAttribute("cartItem", cart.getCartItem());

			int count = cartItemRepository.findByCart(cart).size();
			model.addAttribute("totalPrice", cartItemController.calculateTotal(cart.getCartItem()));
			model.addAttribute("carts", count);
		}
		Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
		Page<Product> page = productRepository.findAll(pageable);

		model.addAttribute("totalPage", page.getTotalPages());
		model.addAttribute("listHome", page.getContent());
		model.addAttribute("listCate", categoryRepository.findAll());

		return "/template/home";
	}

	@GetMapping("/product/list")
	public String listProduct(Model model, @RequestParam(name = "pageSize", defaultValue = "8") Integer pageSize,
			@RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
			HttpSession session) {

		Accounts account = (Accounts) session.getAttribute("userLogged");
		if (account != null) {
			Cart cart = cartRepository.findByAccount(account);
			model.addAttribute("cartItem", cart.getCartItem());

			int count = cartItemRepository.findByCart(cart).size();
			model.addAttribute("totalPrice", cartItemController.calculateTotal(cart.getCartItem()));
			model.addAttribute("carts", count);
		}
		Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
		Page<Product> page = productRepository.findAll(pageable);

		List<Category> category = categoryRepository.findAll();
		model.addAttribute("categories", category);

		model.addAttribute("totalPage", page.getTotalPages());
		model.addAttribute("list", page.getContent());
		return "template/product-listing";
	}

	@GetMapping("/product/detail/{id}")
	public String productDetail(Model model, @PathVariable(name = "id") Integer productId, HttpSession session) {

		Accounts account = (Accounts) session.getAttribute("userLogged");
		if (account != null) {
			Cart cart = cartRepository.findByAccount(account);
			model.addAttribute("cartItem", cart.getCartItem());

			int count = cartItemRepository.findByCart(cart).size();
			model.addAttribute("totalPrice", cartItemController.calculateTotal(cart.getCartItem()));
			model.addAttribute("carts", count);
		}
		model.addAttribute("detail", productRepository.findById(productId).orElse(null));
		model.addAttribute("listSize", sizeRepository.findAll());
		return "template/product-detail";
	}

}
