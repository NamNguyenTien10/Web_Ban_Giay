//package com.spring.service;
//
//import java.util.Collection;
//import java.util.HashMap;
//import java.util.Map;
//
//import org.springframework.stereotype.Service;
//
//import com.spring.entity.CartItem;
//
//@Service
//public class CartItemServiceImpl implements CartItemService {
//
//	Map<Integer, CartItem> maps = new HashMap<>();
//
//	@Override
//	public void add(CartItem item) {
//
////		CartItem cartItem = maps.get(item.getProduct().getId());
////		if (cartItem == null) {
////			maps.put(item.getProduct().getId(), item);
////		} else {
////			cartItem.setQuantity(cartItem.getQuantity() + 1);
////		}
//	}
//
//	@Override
//	public void remove(Integer id) {
//		maps.remove(id);
//	}
//
//	@Override
//	public CartItem update(Integer productId, Integer qty) {
//		CartItem cartItem = maps.get(productId);
//		cartItem.setQuantity(qty);
//
//		return cartItem;
//	}
//
//	@Override
//	public void clear() {
//		maps.clear();
//	}
//
//	@Override
//	public Collection<CartItem> getAllItem() {
//		return maps.values();
//	}
//
//	@Override
//	public int getCount() {
//		return maps.values().size();
//	}
//
//	@Override
//	public double getTotalPrice() {
//		return maps.values().stream().mapToDouble(item -> item.getQuantity() * item.getPrice()).sum();
//	}
//
//}
