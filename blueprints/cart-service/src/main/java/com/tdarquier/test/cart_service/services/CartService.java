package com.tdarquier.test.cart_service.services;

import com.tdarquier.test.cart_service.entities.CartItem;

import java.util.List;

public interface CartService {
    public void addItem(Long userId, Long productId, int quantity);
    public void removeItem(Long userId, Long productId);
    public void clearCart(Long userId);
    public List<CartItem> getAllItems(Long userId);
}