package com.tdarquier.test.cart_service.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tdarquier.test.cart_service.entities.CartItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    private static final String CART_KEY_PREFIX = "cart:";

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public CartServiceImpl(StringRedisTemplate redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    public void addItem(Long userId, Long productId, int quantity) {
        String key = CART_KEY_PREFIX + userId;

        // Obtén todos los elementos del carrito
        List<CartItem> items = getAllItems(userId);

        // Verifica si el producto ya existe en el carrito
        boolean itemExists = false;
        for (CartItem item : items) {
            if (item.getProductId().equals(productId)) {
                // Si existe, incrementa la cantidad
                item.setQuantity(item.getQuantity() + quantity);
                itemExists = true;
                break;
            }
        }

        // Si el producto no existe en el carrito, crea un nuevo CartItem
        if (!itemExists) {
            items.add(new CartItem(productId, quantity, userId));
        }

        // Limpia la lista en Redis y agrega la lista actualizada
        redisTemplate.delete(key);
        items.forEach(item -> {
            try {
                String itemJson = objectMapper.writeValueAsString(item);
                redisTemplate.opsForList().rightPush(key, itemJson);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
    }

    public List<CartItem> getAllItems(Long userId) {
        String key = CART_KEY_PREFIX + userId;

        List<String> itemsJson = redisTemplate.opsForList().range(key, 0, -1);
        if (itemsJson == null) return new ArrayList<>();

        return itemsJson.stream().map(itemJson -> {
            try {
                return objectMapper.readValue(itemJson, CartItem.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return null;
            }
        }).collect(Collectors.toList());
    }

    public void removeItem(Long userId, Long productId) {
        String key = CART_KEY_PREFIX + userId;
        List<CartItem> items = getAllItems(userId);

        // Filtra el producto a eliminar y actualiza la lista en Redis
        items = items.stream()
                .filter(item -> !item.getProductId().equals(productId))
                .collect(Collectors.toList());

        redisTemplate.delete(key);  // Limpia la lista actual

        // Vuelve a insertar los elementos restantes
        items.forEach(item -> addItem(userId, item.getProductId(), item.getQuantity()));
    }

    public void clearCart(Long userId) {
        String key = CART_KEY_PREFIX + userId;
        redisTemplate.delete(key);
    }
}