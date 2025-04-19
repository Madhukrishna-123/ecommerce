package com.ecommerce.order_service.service;

import com.ecommerce.order_service.model.Order;

import java.util.List;

public interface OrderService {
//    Order placeOrder(Order order);
//Order placeOrder(Order order, String email);
Order placeOrder(Order order, String email, String jwtToken);
    List<Order> getOrdersByUserId(Long userId);
    Order getOrderById(Long id);
}

