package com.spring.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.spring.entity.Accounts;
import com.spring.repository.AccountRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {

	@Autowired
	private AccountRepository accountRepository;

	@GetMapping("/login")
	public String getLoginForm(Model model) {

		model.addAttribute("account", new Accounts());
		return "template/colorlib-regform-7/login";
	}

	@PostMapping("/login")
	public String login(Model model, @Validated(com.spring.validate.login.class) @ModelAttribute(name = "account") Accounts account, 
			BindingResult results, // Chứa kết quả kiểm lỗi
			HttpSession session) {

		// Validate
		if (results.hasErrors()) {  // Kiểm tra có lỗi hay không
			return "/template/colorlib-regform-7/login";
		} 

		// Login success
		Accounts accountFromDB = accountRepository.findByUsernameAndPasswords(account.getUsername(),
				account.getPasswords());

		if (accountFromDB != null && accountFromDB.getIs_Admin() == false) {
			session.setAttribute("userLogged", accountFromDB);
			return "redirect:/home";
		} else if (accountFromDB != null && accountFromDB.getIs_Admin() == true) {
			session.setAttribute("userLogged", accountFromDB);
			return "redirect:/admin";
		}

		// Login false
		model.addAttribute("message", "Đăng nhập thất bại");
		return "template/colorlib-regform-7/login";
	}

	@GetMapping("/register")
	public String getRegisterForm(Model model) {

		model.addAttribute("account", new Accounts());
		return "template/colorlib-regform-7/register";
	}

	@PostMapping("/register")
	public String login(Model model,  @Validated(com.spring.validate.register.class)
	@ModelAttribute(name = "account") Accounts account, BindingResult result, HttpServletRequest req,
			HttpSession session) {
		
		if (result.hasErrors()) { // Kiểm tra có lỗi hay không
			return "template/colorlib-regform-7/register";
		}

		account.setIs_Admin(false);
		account.setIs_Active(true);
		accountRepository.save(account);
		session.setAttribute("userLogged", account);
		return "redirect:/home";
	}

	@GetMapping("/logout")
	public String logout(Model model, HttpSession session) {
		session.removeAttribute("userLogged");
		return "redirect:/home";
	}
	
	@GetMapping("/admin/account/list")
	public String listAccount(Model model) {
		
		List<Accounts> acc = accountRepository.findAll();
		model.addAttribute("acc", acc);
		
		return "template/list-accounts";
	}
}
