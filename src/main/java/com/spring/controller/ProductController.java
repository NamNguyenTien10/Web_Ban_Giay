package com.spring.controller;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.spring.entity.Accounts;
import com.spring.entity.Cart;
import com.spring.entity.Category;
import com.spring.entity.Product;
import com.spring.repository.CartItemRepository;
import com.spring.repository.CartRepository;
import com.spring.repository.CategoryRepository;
import com.spring.repository.ProductRepository;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class ProductController {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private CartItemRepository cartItemRepository;

	@Autowired
	private CartItemController cartItemController;
	@GetMapping("/admin")
	public String admin() {

		return "template/admin";
	}

	@GetMapping("/admin/product/list")
	public String findAllProduct(Model model) {

		model.addAttribute("list", productRepository.findAll());
		return "template/list-product";
	}

	@PostMapping("/admin/product/add")
	public String addProduct(Model model, @RequestParam(name = "name") String name,
			@RequestParam(name = "price") Integer price, @RequestParam(name = "image") MultipartFile image,
			@RequestParam(name = "description") String description, @RequestParam(name = "category") String categoryID
	) {

		Category cate = categoryRepository.findById(Integer.valueOf(categoryID)).orElse(null);

		Product product = new Product();
		product.setName(name);
		product.setPrice(price);
		product.setDescription(description);
		product.setCategory(cate);

		if (image.isEmpty()) {
			return "template/add-product";
		}

		Path path = Paths.get("uploads/");
		try {
			InputStream input = image.getInputStream();
			Files.copy(input, path.resolve(image.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
			product.setImage(image.getOriginalFilename().toLowerCase());

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		model.addAttribute("listCate", categoryRepository.findAll());
		productRepository.save(product);
		return "redirect:/admin/product/list";
	}

	@RequestMapping(value = "getimage/{image}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<ByteArrayResource> getImage(@PathVariable("photo") String image) {
		if (!image.equals("") || image != null) {
			try {
				Path filename = Paths.get("uploads", image);
				byte[] buffer = Files.readAllBytes(filename);
				ByteArrayResource byteArrayResource = new ByteArrayResource(buffer);
				return ResponseEntity.ok().contentLength(buffer.length)
						.contentType(MediaType.parseMediaType("image/png")).body(byteArrayResource);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return ResponseEntity.badRequest().build();
	}

	@PostMapping("/admin/product/update")
	public String updateProduct(Model model, 
			@RequestParam(name = "id") String id,
			@RequestParam(name = "name") String name,
			@RequestParam(name = "price") Integer price, @RequestParam(name = "image") MultipartFile image,
			@RequestParam(name = "description") String description,
			@RequestParam(name = "category") String categoryID) {

		Category cate = categoryRepository.findById(Integer.valueOf(categoryID)).orElse(null);

		Product product = new Product();
		product.setName(name);
		product.setPrice(price);
		product.setDescription(description);
		product.setCategory(cate);

		if (image.isEmpty()) {
			return "template/add-product";
		}

		Path path = Paths.get("uploads/");
		try {
			InputStream input = image.getInputStream();
			Files.copy(input, path.resolve(image.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
			product.setImage(image.getOriginalFilename().toLowerCase());

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		Product findProduct = productRepository.findById(Integer.valueOf(id)).orElse(null);
		product.setId(findProduct.getId());
		BeanUtils.copyProperties(product, findProduct);
		productRepository.save(product);
		return "redirect:/admin/product/list";
	}

	@GetMapping("/admin/product/view-add")
	public String getAddProduct(Model model) {
		model.addAttribute("product", new Product());
		model.addAttribute("listCate", categoryRepository.findAll());
		return "template/add-product";
	}

	@GetMapping("/admin/product/detail/{id}")
	public String detailProduct(Model model, @PathVariable(name = "id") Integer productId) {

		model.addAttribute("detailPro", productRepository.findById(productId).orElse(null));
		return "template/detail-product";
	}

	@GetMapping("/admin/product/view-update/{id}")
	public String viewUpdateProduct(Model model, @PathVariable(name = "id") Integer productId) {
		model.addAttribute("detailPro", productRepository.findById(productId).orElse(null));
		model.addAttribute("listCategory", categoryRepository.findAll());
		return "template/update-product";
	}

	@GetMapping("/admin/product/delete/{id}")
	public String deleteProduct(@PathVariable(name = "id") Integer productId) {

		productRepository.deleteById(productId);
		return "redirect:/admin/product/list";
	}

	@RequestMapping("/product/search")
	public String searchProduct(Model model, @RequestParam(name = "keyword") String keyword, HttpSession session) {
		Accounts account = (Accounts) session.getAttribute("userLogged");
		if (account != null) {
			Cart cart = cartRepository.findByAccount(account);
			model.addAttribute("cartItem", cart.getCartItem());

			int count = cartItemRepository.findByCart(cart).size();
			model.addAttribute("totalPrice", cartItemController.calculateTotal(cart.getCartItem()));
			model.addAttribute("carts", count);
		}
		
		List<Category> categories = categoryRepository.findAll();
		model.addAttribute("categories", categories);
		model.addAttribute("listSearch", productRepository.findByKeyword(keyword));
		return "template/product-search";
	}

	@RequestMapping("/product/price")
	public String getAllProductByPrice(Model model, @RequestParam(name = "min") Integer min,
			@RequestParam(name = "max") Integer max,
			@RequestParam(name = "pageSize", defaultValue = "8") Integer pageSize,
			@RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum, HttpSession session) {

		Accounts account = (Accounts) session.getAttribute("userLogged");
		if (account != null) {
			Cart cart = cartRepository.findByAccount(account);
			model.addAttribute("cartItem", cart.getCartItem());

			int count = cartItemRepository.findByCart(cart).size();
			model.addAttribute("totalPrice", cartItemController.calculateTotal(cart.getCartItem()));
			model.addAttribute("carts", count);
		}
		
		List<Category> categories = categoryRepository.findAll();
		model.addAttribute("categories", categories);

		Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
		Page<Product> page = productRepository.findByPrice(min, max, pageable);

		String minPrice = String.valueOf(min);
		String maxPrice = String.valueOf(max);

		model.addAttribute("min", minPrice);
		model.addAttribute("max", maxPrice);
		model.addAttribute("totalPage", page.getTotalPages());
		model.addAttribute("products", page.getContent());
		// model.addAttribute("products", productRepository.findByPrice(min, max));
		return "template/product-fillter-price.html";
	}

	@GetMapping("/product/shortbyname")
	public String shortProductByName(Model model, @RequestParam(name = "pageSize", defaultValue = "8") Integer pageSize,
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
		Page<Product> page = productRepository.findAllProductsShortByName(pageable);

		model.addAttribute("totalPage", page.getTotalPages());
		model.addAttribute("listShortByName", page.getContent());

		List<Category> categories = categoryRepository.findAll();
		model.addAttribute("categories", categories);

		return "template/product-shortbyname";
	}

	@GetMapping("/product/shortbypriceasc")
	public String shortProductByPriceASC(Model model,
			@RequestParam(name = "pageSize", defaultValue = "8") Integer pageSize,
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
		Page<Product> page = productRepository.findAllProductsShortByPriceASC(pageable);

		model.addAttribute("totalPage", page.getTotalPages());
		model.addAttribute("listShortByPriceASC", page.getContent());

		List<Category> categories = categoryRepository.findAll();
		model.addAttribute("categories", categories);

		return "template/product-shortbypriceasc";
	}

	@GetMapping("/product/shortbypricedesc")
	public String shortProductByPriceDESC(Model model,
			@RequestParam(name = "pageSize", defaultValue = "8") Integer pageSize,
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
		Page<Product> page = productRepository.findAllProductsShortByPriceDESC(pageable);

		model.addAttribute("totalPage", page.getTotalPages());
		model.addAttribute("listShortByPriceDESC", page.getContent());

		List<Category> categories = categoryRepository.findAll();
		model.addAttribute("categories", categories);

		return "template/product-shortbypricedesc";
	}

	@GetMapping("/product/category/{name}")
	public String getAllProductById(Model model, @PathVariable String name, HttpSession session) {

		Accounts account = (Accounts) session.getAttribute("userLogged");
		if (account != null) {
			Cart cart = cartRepository.findByAccount(account);
			model.addAttribute("cartItem", cart.getCartItem());

			int count = cartItemRepository.findByCart(cart).size();
			model.addAttribute("totalPrice", cartItemController.calculateTotal(cart.getCartItem()));
			model.addAttribute("carts", count);
		}
		
		List<Category> categories = categoryRepository.findAll();
		model.addAttribute("categories", categories);

		List<Product> products = productRepository.findAllByCategoryName(name);
		model.addAttribute("products", products);

		return "template/category-detail";
	}

}
