package com.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.entity.Accounts;
import com.spring.entity.Bill;
import com.spring.entity.BillItem;
import com.spring.entity.Product;

public interface BillItemRepository extends JpaRepository<BillItem, Integer> {


	BillItem findByBillAndProduct(Bill bill, Product product);

}
