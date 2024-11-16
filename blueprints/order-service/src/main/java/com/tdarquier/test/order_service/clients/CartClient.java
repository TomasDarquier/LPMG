package com.tdarquier.test.order_service.clients;

import com.tdarquier.test.order_service.entities.CartItem;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface CartClient{

    @PostMapping("/{userId}/add")
    void addItem(@PathVariable Long userId, @RequestParam Long productId, @RequestParam int quantity);

    @GetMapping("/{userId}/items")
    List<CartItem> getAllItems(@PathVariable Long userId);

    @DeleteMapping("/{userId}/remove")
    void removeItem(@PathVariable Long userId, @RequestParam Long productId);

    @DeleteMapping("/{userId}/clear")
    void clearCart(@PathVariable Long userId);
}
