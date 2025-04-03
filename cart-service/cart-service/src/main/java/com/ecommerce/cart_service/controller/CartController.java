package com.ecommerce.cart_service.controller;

import com.ecommerce.cart_service.exception.ItemNotFoundException;
import com.ecommerce.cart_service.model.CartItem;
import com.ecommerce.cart_service.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<CartItem> addItem(@RequestBody CartItem item) {
        return new ResponseEntity<>(cartService.addToCart(item), HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<CartItem>> viewCart(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.getUserCart(userId));
    }

    @DeleteMapping("/remove")
    public ResponseEntity<String> removeItem(@RequestParam Long userId, @RequestParam Long productId) {
//        cartService.removeItem(userId, productId);
//        return ResponseEntity.ok("Item removed from cart");
        try {
            cartService.removeItem(userId, productId);
            return ResponseEntity.ok("Item removed from cart");
        } catch (ItemNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }

    @DeleteMapping("/clear/{userId}")
    public ResponseEntity<String> clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.ok("Cart cleared");
    }
}

