package com.tdarquier.test.order_service.controllers;

//
import com.tdarquier.test.order_service.clients.ProductClient;
import com.tdarquier.test.order_service.dtos.ProductDto;
import com.tdarquier.test.order_service.entities.CartItem;
import java.util.concurrent.atomic.AtomicReference;
//
import com.tdarquier.test.order_service.entities.Order;
import com.tdarquier.test.order_service.enums.Category;
import com.tdarquier.test.order_service.services.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    /// ////////////////////////////////////////////////////////////////
    private ProductClient productClient;

    @PostMapping("/from/cart")
    public Order createOrderFromCartItems(@RequestBody List<CartItem> items, @RequestParam String orderName, @RequestParam String category){
        AtomicReference<Float> total = new AtomicReference<>((float) 0);
        items.forEach(item -> {
            ProductDto product = productClient.getProductDtoById(item.getProductId());
            total.set((float) (product.price() * item.getQuantity()));
        });
        Order order = new Order();
        order.setOrderCategory(Category.valueOf(category));
        order.setOrderPrice(total.get());
        order.setOrderName(orderName);

        return order;
    }

    @GetMapping("")
    public List<Order> getOrders() {
        return orderService.getOrders();
    }

    @GetMapping("/{orderId}")
    public Order getOrder(@PathVariable long orderId) {
        return orderService.getOrder(orderId);
    }

    @PostMapping("")
    public Order createOrder(@RequestBody Order order) {
        return orderService.createOrder(order);
    }

    @PutMapping("")
    public Order updateOrder(@RequestBody Order order) {
        return orderService.updateOrder(order);
    }

    @DeleteMapping("/{orderId}")
    public void deleteOrder(@PathVariable long orderId) {
        orderService.deleteOrder(orderId);
    }

}