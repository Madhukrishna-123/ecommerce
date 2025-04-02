package com.ecommerce.product_service.util;

import com.ecommerce.product_service.model.Product;
import com.ecommerce.product_service.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductDataLoader implements CommandLineRunner {

    private final ProductRepository productRepository;

    public ProductDataLoader(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) {
        if (productRepository.count() == 0) {
            List<Product> products = List.of(
//                    new Product("Laptop", "High-performance laptop", "Electronics", 75000.0, 10, "https://example.com/images/laptop.jpg"),
//                    new Product("Smartphone", "Latest model smartphone", "Electronics", 35000.0, 15, "https://example.com/images/smartphone.jpg"),
//                    new Product("Headphones", "Wireless headphones", "Accessories", 3000.0, 20, "https://example.com/images/headphones.jpg"),
//                    new Product("Shoes", "Comfortable running shoes", "Footwear", 2000.0, 25, "https://example.com/images/shoes.jpg"),
//                    new Product("Backpack", "Durable travel backpack", "Accessories", 1500.0, 30, "https://example.com/images/backpack.jpg"),
//                    new Product("Watch", "Smartwatch with health features", "Wearable", 5000.0, 10, "https://example.com/images/watch.jpg"),
//                    new Product("Tablet", "10-inch tablet with LTE", "Electronics", 20000.0, 8, "https://example.com/images/tablet.jpg"),
//                    new Product("Keyboard", "Mechanical keyboard", "Accessories", 2500.0, 18, "https://example.com/images/keyboard.jpg"),
//                    new Product("Monitor", "4K Ultra HD Monitor", "Electronics", 30000.0, 5, "https://example.com/images/monitor.jpg"),
//                    new Product("Camera", "Digital SLR Camera", "Electronics", 45000.0, 7, "https://example.com/images/
                    new Product("Laptop", "High-performance laptop", "Electronics", 75000.0, 10, "https://images.unsplash.com/photo-1517336714731-489689fd1ca8"),
                    new Product("Smartphone", "Latest model smartphone", "Electronics", 35000.0, 15, "https://images.unsplash.com/photo-1511707171634-5f897ff02aa9"),
                    new Product("Headphones", "Wireless headphones", "Accessories", 3000.0, 20, "https://images.unsplash.com/photo-1516321497487-e288fb19713f"),
                    new Product("Backpack", "Durable travel backpack", "Accessories", 1500.0, 30, "https://images.unsplash.com/photo-1542291026-7eec264c27ff"),
                    new Product("Keyboard", "Mechanical keyboard", "Accessories", 2500.0, 18, "https://images.unsplash.com/photo-1511385348-a52b4a160dc2"),
                    new Product("Monitor", "4K Ultra HD Monitor", "Electronics", 30000.0, 5, "https://images.unsplash.com/photo-1517336714731-489689fd1ca8"),
                    new Product("Camera", "Digital SLR Camera", "Electronics", 45000.0, 7, "https://cdn.pixabay.com/photo/2017/08/07/01/41/camera-2598507_1280.jpg"),
                    new Product("Watch", "Smartwatch with health features", "Wearable", 5000.0, 10, "https://cdn.pixabay.com/photo/2016/11/29/13/39/analog-watch-1869928_1280.jpg"),
                    new Product("Tablet", "10-inch tablet with LTE", "Electronics", 20000.0, 8, "https://cdn.pixabay.com/photo/2017/08/10/08/23/ipad-2619958_1280.jpg"),
                    new Product("Shoes", "Comfortable running shoes", "Footwear", 2000.0, 25, "https://cdn.pixabay.com/photo/2016/11/19/11/33/footwear-1838767_1280.jpg")

            );

            productRepository.saveAll(products);

        } else {
            System.out.println("Products already exist.");
        }
    }
}