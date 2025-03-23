package com.ecommerce.cart_service.service;

import com.ecommerce.cart_service.model.CartItem;

import java.util.List;

public interface CartService {
    CartItem addToCart(CartItem item);
    List<CartItem> getUserCart(Long userId);
    void removeItem(Long userId, Long productId);
    void clearCart(Long userId);
}
