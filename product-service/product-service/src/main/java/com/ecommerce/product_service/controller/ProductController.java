package com.ecommerce.product_service.controller;

import com.ecommerce.product_service.model.Product;
import com.ecommerce.product_service.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/addproduct")
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {

        return new ResponseEntity<>(productService.saveProduct(product), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAll() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> search(@RequestParam(required = false) String name,
                                                @RequestParam(required = false) String category) {
        if (name != null) return ResponseEntity.ok(productService.searchByName(name));
        if (category != null) return ResponseEntity.ok(productService.searchByCategory(category));
        return ResponseEntity.badRequest().build();
    }
}
