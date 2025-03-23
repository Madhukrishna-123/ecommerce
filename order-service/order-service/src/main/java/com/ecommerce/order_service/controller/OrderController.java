package com.ecommerce.order_service.controller;

import com.ecommerce.order_service.jwt.JwtUtil;
import com.ecommerce.order_service.model.Order;
import com.ecommerce.order_service.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private JwtUtil jwtUtil;

//    @PostMapping("/place")
//    public ResponseEntity<Order> placeOrder(@RequestBody Order order) {
//        return new ResponseEntity<>(orderService.placeOrder(order), HttpStatus.CREATED);
//    }
@PostMapping("/place")
public ResponseEntity<Order> placeOrder(@RequestBody Order order, @RequestHeader("Authorization") String token) {
    // Strip "Bearer " prefix from token
    String jwt = token.substring(7);

    // Extract email from token
    String email = jwtUtil.extractUsername(jwt);

    // Place the order and send confirmation
    Order savedOrder = orderService.placeOrder(order, email);

    return new ResponseEntity<>(savedOrder, HttpStatus.CREATED);
}


    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getUserOrders(@PathVariable Long userId) {
        return ResponseEntity.ok(orderService.getOrdersByUserId(userId));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }
}
