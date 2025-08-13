package com.example.demo.controller;

import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "http://localhost:3000")
public class ProductController {

    private final Map<Long, Product> productStore = new HashMap<>();
    private final AtomicLong idCounter = new AtomicLong();

    @GetMapping
    public List<Product> getAllProducts() {
        return new ArrayList<>(productStore.values());
    }

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable Long id) {
        return productStore.get(id);
    }

    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        long id = idCounter.incrementAndGet();
        product.setId(id);
        productStore.put(id, product);
        return product;
    }

    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product product) {
        product.setId(id);
        productStore.put(id, product);
        return product;
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productStore.remove(id);
    }

    static class Product {
        private Long id;
        private String name;
        private String description;
        private Double price;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public Double getPrice() { return price; }
        public void setPrice(Double price) { this.price = price; }

        public Product() {}
        public Product(Long id, String name, String description, Double price) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.price = price;
        }
    }
}
