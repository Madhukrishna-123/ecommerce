package com.ecommerce.order_service.service;

import com.ecommerce.order_service.dto.EmailRequest;
import com.ecommerce.order_service.dto.UserResponse;
import com.ecommerce.order_service.model.Order;
import com.ecommerce.order_service.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private RestTemplate restTemplate;


//    @Override
//    public Order placeOrder(Order order ,String email) {
//        order.setOrderDate(LocalDate.now());
//        return orderRepository.save(order);
//    }
@Override
public Order placeOrder(Order order, String email) {
    order.setOrderDate(LocalDate.now());
    Order savedOrder = orderRepository.save(order);

    // Call user-service to get username
    String userServiceUrl = "http://user-service/api/users/profile/" + email;
    ResponseEntity<UserResponse> response = restTemplate.getForEntity(userServiceUrl, UserResponse.class);
    String username = response.getBody().getUsername();

    // Send email
    EmailRequest emailReq = new EmailRequest();
    emailReq.setTo(email);
    emailReq.setSubject("Order Confirmation");
    emailReq.setMessage("Hi " + username + ", your order #" + savedOrder.getId() + " is confirmed!");

    restTemplate.postForObject("http://notification-service/api/notify/send", emailReq, String.class);

    return savedOrder;
}


    @Override
    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }
}
