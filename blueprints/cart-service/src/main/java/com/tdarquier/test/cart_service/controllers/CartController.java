package com.tdarquier.test.cart_service.controllers;

import com.tdarquier.test.cart_service.entities.CartItem;
import com.tdarquier.test.cart_service.services.CartService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/{userId}/add")
    public void addItem(@PathVariable Long userId, @RequestParam Long productId, @RequestParam int quantity) {
        cartService.addItem(userId, productId, quantity);
    }

    @GetMapping("/{userId}/items")
    public List<CartItem> getAllItems(@PathVariable Long userId) {
        return cartService.getAllItems(userId);
    }

    @DeleteMapping("/{userId}/remove")
    public void removeItem(@PathVariable Long userId, @RequestParam Long productId) {
        cartService.removeItem(userId, productId);
    }

    @DeleteMapping("/{userId}/clear")
    public void clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
    }
}