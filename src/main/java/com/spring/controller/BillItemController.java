package com.spring.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.spring.entity.Accounts;
import com.spring.entity.Bill;
import com.spring.entity.BillItem;
import com.spring.entity.Cart;
import com.spring.entity.CartItem;
import com.spring.entity.Product;
import com.spring.repository.BillItemRepository;
import com.spring.repository.BillRepository;
import com.spring.repository.CartItemRepository;
import com.spring.repository.CartRepository;
import com.spring.repository.ProductRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class BillItemController {

	
	@Autowired
	private BillItemRepository billItemRepository;

	@Autowired
	private BillRepository billRepository;

	@Autowired
	private CartItemRepository cartItemRepository;
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	
	@RequestMapping("/bill")
	public String bill(Model model, HttpSession session) {

		Accounts account = (Accounts) session.getAttribute("userLogged");
		if (account == null) {
			return "redirect:/login";
		}
		Bill bills = billRepository.findByAccountId(account.getId());
		if (bills == null) {
			bills = new Bill();
			bills.setAccount(account);
			bills.setCreateDate(new Date());
			billRepository.save(bills);
		}
		
		Bill bill = billRepository.findByAccount(account);
		model.addAttribute("billItem", bill.getBillItem());
		model.addAttribute("createDate", bills.getCreateDate());
		model.addAttribute("bill", bills.getId());
		return "template/checkout";
	}


	@GetMapping("/buy/{id}")
	public String buy(Model model,@PathVariable(name="id") Integer productId,@PathVariable(name="id") Integer billId, HttpSession session) {

		Product product = productRepository.getProductById(productId);

		Accounts account = (Accounts) session.getAttribute("userLogged");
		if (account == null) {
			return "redirect:/login";
		}
		
		Bill bill = billRepository.findByAccount(account);
		if (bill == null) {
			bill = new Bill();
			bill.setAccount(account);
			bill.setCreateDate(new Date());
			billRepository.save(bill);
		}
		
		Cart cart = cartRepository.findByAccount(account);
		CartItem carts = cartItemRepository.findByCartAndProduct(cart, product);
		
		BillItem bills = new BillItem();
		bills.setProduct(product);
		bills.setPrice(product.getPrice());
		bills.setQuantity(carts.getQuantity());
		bills.setBill(bill);
		billItemRepository.save(bills);
		
		cartItemRepository.deleteById(product.getId());
		
		model.addAttribute("bill", billRepository.findById(Integer.valueOf(billId)).orElse(null));
		return "redirect:/bill";
	}
	
	@PostMapping("/pay")
	public String payBill(Model model, 
			@RequestParam(name="id") Integer billId,
			@RequestParam(name="fullname") String fullname,
			@RequestParam(name="phone") String phone,
			@RequestParam(name="address") String address,
			HttpSession session
			) {
		Accounts account = (Accounts) session.getAttribute("userLogged");
		if (account == null) {
			return "redirect:/login";
		}
		Bill bill = new Bill();
		bill.setAccount(account);
		bill.setRecipientName(fullname);
		bill.setPhoneNumber(phone);
		bill.setAddress(address);
		Bill findBill = billRepository.findById(Integer.valueOf(billId)).orElse(null);
		
		bill.setId(findBill.getId());
		BeanUtils.copyProperties(bill, findBill);
		billRepository.save(findBill);
		

		
		return "redirect:/home";
		
	}
	
}
