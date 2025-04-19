package com.ecommerce.order_service.service;

import com.ecommerce.order_service.dto.EmailRequest;
import com.ecommerce.order_service.model.Order;
import com.ecommerce.order_service.repository.OrderRepository;
import com.ecommerce.user_service.model.User;
import com.ecommerce.order_service.jwt.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Order placeOrder(Order order, String email, String jwtToken) {
        order.setOrderDate(LocalDate.now());
        Order savedOrder = orderRepository.save(order);

        try {
            String userServiceUrl = "http://user-service/api/users/userdetails";
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + jwtToken);

            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<User> response = restTemplate.exchange(
                    userServiceUrl,
                    HttpMethod.GET,
                    entity,
                    User.class
            );

            User user = response.getBody();
            String username = user != null ? user.getFirstName() : "Customer";

            EmailRequest emailReq = new EmailRequest();
            emailReq.setTo(email);
            emailReq.setSubject("Order Confirmation");
            emailReq.setMessage("Hi " + username + ", your order " + savedOrder.getProductName() + " is confirmed!");

            restTemplate.postForObject("http://notification-service/api/notify/send", emailReq, String.class);

        } catch (Exception e) {
            log.error("Failed to fetch user details or send email: {}", e.getMessage());
        }

        return savedOrder;
    }

    @Override
    public List<Order> getOrdersByUserId(Long userId) {
        log.info("Fetching orders for user ID: {}", userId);
        return orderRepository.findByUserId(userId);
    }

    @Override
    public Order getOrderById(Long id) {
        log.info("Fetching order with ID: {}", id);
        return orderRepository.findById(id).orElse(null);
    }
}
