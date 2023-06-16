package com.spring.entity;

import java.util.Date;
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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Data
@Entity
@Table(name="Bill")
public class Bill {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="account_id")
	private Accounts account;
	
	@ManyToOne
	@JoinColumn(name="product_id")
	private Product product;
	
	@Temporal(TemporalType.DATE)
	@Column(name="create_date")
	Date createDate = new Date();
	
	@Column(name="recipient_name")
	private String recipientName;
	
	@Column(name="address")
	private String address;
	
	@Column(name="phone_number")
	private String phoneNumber;
	
	@Column(name="available")
	private Boolean available;
	
	@OneToMany(mappedBy = "bill")
	private List<BillItem> billItem;

	public Bill() {
		super();
	}

	public Bill(Integer id, Accounts account, Product product, Date createDate, String recipientName, String address,
			String phoneNumber, Boolean available, List<BillItem> billItem) {
		super();
		this.id = id;
		this.account = account;
		this.product = product;
		this.createDate = createDate;
		this.recipientName = recipientName;
		this.address = address;
		this.phoneNumber = phoneNumber;
		this.available = available;
		this.billItem = billItem;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Accounts getAccount() {
		return account;
	}

	public void setAccount(Accounts account) {
		this.account = account;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getRecipientName() {
		return recipientName;
	}

	public void setRecipientName(String recipientName) {
		this.recipientName = recipientName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Boolean getAvailable() {
		return available;
	}

	public void setAvailable(Boolean available) {
		this.available = available;
	}

	public List<BillItem> getBillItem() {
		return billItem;
	}

	public void setBillItem(List<BillItem> billItem) {
		this.billItem = billItem;
	}
	
	
}