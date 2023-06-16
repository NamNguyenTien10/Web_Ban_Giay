package com.spring.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.spring.entity.Accounts;
import com.spring.entity.Cart;
import com.spring.entity.CartItem;
import com.spring.entity.Product;
import com.spring.repository.AccountRepository;
import com.spring.repository.CartItemRepository;
import com.spring.repository.CartRepository;
import com.spring.repository.ProductRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class CartItemController {

	@Autowired
	private ProductRepository productRepository;


	@Autowired
	private CartItemRepository cartItemRepository;

	@Autowired
	private CartRepository cartRepository;

	@GetMapping("/cart")
	public String viewCart(Model model, HttpSession session) {

		Accounts account = (Accounts) session.getAttribute("userLogged");
		if (account == null) {
			return "redirect:/login";
		}

//		List<Cart> carts = cartRepository.findByAccountId(account.getId());
		Cart carts = cartRepository.findByAccountId(account.getId());
		if (carts == null) {
			carts = new Cart();
			carts.setAccount(account);
			cartRepository.save(carts);
		}
		
		Cart cart = cartRepository.findByAccount(account);
		model.addAttribute("cartItem", cart.getCartItem());

		int count = cartItemRepository.findByCart(cart).size();
		model.addAttribute("totalPrice", this.calculateTotal(cart.getCartItem()));
		model.addAttribute("carts", count);
		model.addAttribute("cartId", cartRepository.findById(cart.getId()).orElse(null));
		return "template/cart";
	}

	@GetMapping("/addToCart/{id}")
	public String addCart(Model model, @PathVariable(name = "id") Integer productId, HttpSession session,
			Integer cartId) {

		Product product = productRepository.getProductById(productId);

		Accounts account = (Accounts) session.getAttribute("userLogged");
		if (account == null) {
			return "redirect:/login";
		}
		Cart cart = cartRepository.findByAccount(account);
		
		CartItem carts = cartItemRepository.findByCartAndProduct(cart, product);
		if (cart == null) {
			cart = new Cart();
			cart.setAccount(account);
			cartRepository.save(cart);
		} 

		if (carts != null) {
			carts.setQuantity(carts.getQuantity() + 1);
		} else {
			carts = new CartItem();
			carts.setProduct(product);
			carts.setPrice(product.getPrice());
			carts.setQuantity(1);
			carts.setCart(cart);
		}
		
		cartItemRepository.save(carts);
		return "redirect:/cart";
	}

	@GetMapping("/removeCart/{id}")
	public String removeCart(@PathVariable(name="id") Integer productId) {
		
		cartItemRepository.deleteById(productId);
		return "redirect:/cart";
	}

	
	public double calculateTotal(List<CartItem> cartItems) {
	    Integer total = 0;
	    for (CartItem item : cartItems) {
	        total += item.getPrice() * item.getQuantity();
	    }
	    return total;
	}
	

}
