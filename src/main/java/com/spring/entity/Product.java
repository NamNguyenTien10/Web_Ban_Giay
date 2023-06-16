package com.spring.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "Product")

public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "name")
	@NotBlank(message = "Chưa nhập name")
	private String name;

	@Column(name = "price")
	@NotNull(message = "Chưa nhập price")
	private Integer price;

	@NotBlank(message = "Chưa nhập image")
	@Column(name = "image")
	private String image;

	@Column(name = "description")
	@NotBlank(message = "Chưa nhập description")
	private String description;

	@Column(name = "available")
	private String available;

	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;
	
	@ManyToOne
	@JoinColumn(name = "size_id")
	private Size size;

	@OneToMany(mappedBy = "product")
	private List<Cart> cart;

	@OneToMany(mappedBy = "product")
	private List<CartItem> cartItem;

	@OneToMany(mappedBy = "product")
	private List<BillItem> billItem;

	public Integer getPrice() {
		return price;
	}

	public Product() {
		super();
	}

	

	public Product(Integer id, @NotBlank(message = "Chưa nhập name") String name,
			@NotNull(message = "Chưa nhập price") Integer price, @NotBlank(message = "Chưa nhập image") String image,
			@NotBlank(message = "Chưa nhập description") String description, String available, Category category,
			Size size, List<Cart> cart, List<CartItem> cartItem, List<BillItem> billItem) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
		this.image = image;
		this.description = description;
		this.available = available;
		this.category = category;
		this.size = size;
		this.cart = cart;
		this.cartItem = cartItem;
		this.billItem = billItem;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAvailable() {
		return available;
	}

	public void setAvailable(String available) {
		this.available = available;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public List<Cart> getCart() {
		return cart;
	}

	public void setCart(List<Cart> cart) {
		this.cart = cart;
	}

	public List<CartItem> getCartItem() {
		return cartItem;
	}

	public void setCartItem(List<CartItem> cartItem) {
		this.cartItem = cartItem;
	}

	public List<BillItem> getBillItem() {
		return billItem;
	}

	public void setBillItem(List<BillItem> billItem) {
		this.billItem = billItem;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Size getSize() {
		return size;
	}

	public void setSize(Size size) {
		this.size = size;
	}

	
}
