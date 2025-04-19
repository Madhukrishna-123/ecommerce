package com.ecommerce.order_service.controller;

import com.ecommerce.order_service.dto.EmailRequest;
import com.ecommerce.order_service.jwt.JwtUtil;
import com.ecommerce.order_service.model.Order;
import com.ecommerce.order_service.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@Slf4j
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);
    @Autowired
    private OrderService orderService;
    @Autowired
    private JwtUtil jwtUtil;

    @Value("${user.service.url}")
    private String userServiceUrl;

    @Value("${notification.service.url}")
    private String notificationServiceUrl;

    @PostMapping("/place")
    public ResponseEntity<Order> placeOrder(@RequestBody Order order, @RequestHeader("Authorization") String token) {
        // Strip "Bearer " prefix from token
//        log.info("Placing order. Token: {}", token);
        log.info("placing order . Token : {}",token);
        String jwt = token.substring(7);  // Extract JWT token
        String email = jwtUtil.extractUsername(jwt);  // Extract email from token

        log.info("Placing order for user: {}", email);

        // Place the order and send confirmation
        Order savedOrder = orderService.placeOrder(order, email, jwt);

        log.info("Order placed successfully: Order ID {}", savedOrder.getId());
        return new ResponseEntity<>(savedOrder, HttpStatus.CREATED);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getUserOrders(@PathVariable Long userId) {
        log.info("Fetching orders for user ID: {}", userId);
        return ResponseEntity.ok(orderService.getOrdersByUserId(userId));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long orderId) {
        log.info("Fetching order details for Order ID: {}", orderId);
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }

    // Test methods (for debugging purposes)
    @GetMapping("/test")
    public String test() {
        return "HELLO MADHU";
    }

}
