package com.tdarquier.test.order_service.services;

import com.tdarquier.test.order_service.entities.Order;

import java.util.List;

public interface OrderService {
    List<Order> getOrders();
    Order getOrder(long orderId);
    Order createOrder(Order order);
    Order updateOrder(Order order);
    void deleteOrder(long orderId);
}