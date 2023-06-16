package com.spring.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.spring.entity.Cart;
import com.spring.entity.CartItem;
import com.spring.entity.Product;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

	CartItem findByCartIdAndProductId(Integer cartId, Integer productId);

	List<CartItem> findByCart(Cart cart);

	CartItem findByCartAndProduct(Cart cart, Product product);

	CartItem findByCartId(Integer cartId);





//	@Query("SELECT c FROM cart_item c WHERE c.cart.id = :cartId AND c.product.id = :productId")
//	public List<CartItem> findByCartIdAndProductId(@Param("cartId") Integer cartId, @Param("productId") Integer productId);
}
