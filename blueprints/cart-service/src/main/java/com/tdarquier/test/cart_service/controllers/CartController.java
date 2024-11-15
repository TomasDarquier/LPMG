package com.tdarquier.test.cart_service.controllers;

import com.tdarquier.test.cart_service.clients.ProductClient;
import com.tdarquier.test.cart_service.dtos.ProductDto;
import com.tdarquier.test.cart_service.entities.CartItem;
import com.tdarquier.test.cart_service.services.CartService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;
    final ProductClient productClient;

    public CartController(CartService cartService, ProductClient productClient) {
        this.cartService = cartService;
        this.productClient = productClient;
    }

    @GetMapping("/{userId}/total")
    public Double getTotal(@PathVariable Long userId) {
        AtomicReference<Double> total = new AtomicReference<>(0.0);
        cartService.getAllItems(userId).forEach( cartItem -> {
            ProductDto dto = productClient.getProductDtoById(cartItem.getProductId());
            total.set(total.get() + (dto.price() * cartItem.getQuantity()));
        });
        return total.get();
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