package com.example.demo.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "http://localhost:3000")
public class ProductController {

    @GetMapping
    public List<Product> getAllProducts() {
        return List.of(
                new Product(1L, "Laptop", "Gaming Laptop", 1200.0),
                new Product(2L, "T-Shirt", "Cotton T-Shirt", 25.0)
        );
    }

    // Basit Product sınıfı
    static class Product {
        public Long id;
        public String name;
        public String description;
        public Double price;

        public Product(Long id, String name, String description, Double price) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.price = price;
        }
    }
}
