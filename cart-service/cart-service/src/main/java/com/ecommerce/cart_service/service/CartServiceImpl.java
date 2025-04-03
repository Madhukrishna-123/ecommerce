package com.ecommerce.cart_service.service;

import com.ecommerce.cart_service.exception.ItemNotFoundException;
import com.ecommerce.cart_service.model.CartItem;
import com.ecommerce.cart_service.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Override
    public CartItem addToCart(CartItem item) {
        return cartRepository.save(item);
    }

    @Override
    public List<CartItem> getUserCart(Long userId) {
        return cartRepository.findByUserId(userId);
    }

//    @Override
//    public void removeItem(Long userId, Long productId) {
//        cartRepository.deleteByUserIdAndProductId(userId, productId);
//    }
@Transactional
@Override
public void removeItem(Long userId, Long productId) {
    // Check if the item exists first (optional, but a good practice)
    Optional<CartItem> cartItem = cartRepository.findByUserIdAndProductId(userId, productId);

    if (cartItem.isPresent()) {
        // If the item exists, delete it
        cartRepository.deleteByUserIdAndProductId(userId, productId);
    } else {
        // If item doesn't exist, throw an exception (custom exception or use IllegalArgumentException)
        throw new ItemNotFoundException("Item not found in cart");
    }
}

    @Transactional
    @Override
    public void clearCart(Long userId) {
        cartRepository.deleteByUserId(userId);
    }
}
