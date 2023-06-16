package com.spring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.entity.Accounts;
import com.spring.entity.Cart;
import com.spring.entity.CartItem;
import com.spring.entity.Product;

public interface CartRepository extends JpaRepository<Cart, Integer> {

	Cart findByAccount(Accounts account);

	Cart findByAccountId(Integer accountId);

	Cart findByAccountAndProduct(Accounts account, Product product);

	Cart getCartById(Integer cartId);

	CartItem getProductById(Integer productId);

	
}
