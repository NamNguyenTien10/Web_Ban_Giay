package com.spring.entity;


import java.util.List;

import com.spring.validate.login;
import com.spring.validate.register;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity
@Table(name="Accounts")
public class Accounts {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	
	@NotBlank(message = "Nhập username đê", groups = {login.class, register.class})
	@Column(name="username")
	private String username;
	
	@NotBlank(message = "Nhập password đê", groups = {login.class, register.class})
	@Column(name="passwords")
	private String passwords;
	
	@NotBlank(message = "Nhập fullname đê", groups = {register.class})
	@Column(name="fullname")
	private String fullname;
	
	@NotBlank(message = "Nhập email đê", groups = {register.class})
	@Column(name="email")
	private String email;
	
	@Column(name="photo")
	private String photo;
	
	@Column(name="is_Admin")
	private Boolean is_Admin;
	
	@Column(name="is_Active")
	private Boolean is_Active;

	@OneToMany(mappedBy = "account")
	private List<Cart> cart;
	
	@OneToMany(mappedBy = "account")
	private List<Bill> bill;
	
	public Accounts() {
		super();
	}

	public Accounts(Integer id, String username, String passwords, String fullname, String email, String photo,
			Boolean is_Admin, Boolean is_Active, List<Cart> cart, List<Bill> bill) {
		super();
		this.id = id;
		this.username = username;
		this.passwords = passwords;
		this.fullname = fullname;
		this.email = email;
		this.photo = photo;
		this.is_Admin = is_Admin;
		this.is_Active = is_Active;
		this.cart = cart;
		this.bill = bill;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPasswords() {
		return passwords;
	}

	public void setPasswords(String passwords) {
		this.passwords = passwords;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public Boolean getIs_Admin() {
		return is_Admin;
	}

	public void setIs_Admin(Boolean isAdmin) {
		this.is_Admin = isAdmin;
	}

	public Boolean getIs_Active() {
		return is_Active;
	}

	public void setIs_Active(Boolean isActive) {
		this.is_Active = isActive;
	}

	public List<Cart> getCart() {
		return cart;
	}

	public void setCart(List<Cart> cart) {
		this.cart = cart;
	}

	public List<Bill> getBill() {
		return bill;
	}

	public void setBill(List<Bill> bill) {
		this.bill = bill;
	}

	
	
	
	
}
