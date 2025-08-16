package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.entity.Product;
import com.example.demo.service.OrderService;
import com.example.demo.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequestMapping("/orders")
public class OrderPanelController {

    private final ProductRepository productRepository;

    public OrderPanelController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/panel")
    public String orderPanel(Model model, HttpSession session) {
        List<Product> products = productRepository.findAll();

        // Sepet
        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
        if (cart == null) {
            cart = new HashMap<>();
        }

        List<Map.Entry<Product, Integer>> cartProducts = new ArrayList<>();
        double cartTotal = 0;
        for (Map.Entry<Long, Integer> entry : cart.entrySet()) {
            productRepository.findById(entry.getKey()).ifPresent(product -> {
                cartProducts.add(new AbstractMap.SimpleEntry<>(product, entry.getValue()));
            });
            cartTotal += productRepository.findById(entry.getKey())
                    .map(p -> p.getPrice() * entry.getValue())
                    .orElse(0.0);
        }

        // Favoriler
        Set<Long> favorites = (Set<Long>) session.getAttribute("favorites");
        if (favorites == null) {
            favorites = new HashSet<>();
        }
        List<Product> favoriteProducts = productRepository.findAllById(favorites);

        model.addAttribute("products", products);
        model.addAttribute("cartProducts", cartProducts);
        model.addAttribute("favoriteProducts", favoriteProducts);
        model.addAttribute("cartTotal", cartTotal);

        return "order-panel";
    }

    @PostMapping("/cart/add")
    public String addToCart(@RequestParam Long productId, @RequestParam int quantity, HttpSession session) {
        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
        if (cart == null) {
            cart = new HashMap<>();
        }
        cart.put(productId, cart.getOrDefault(productId, 0) + quantity);
        session.setAttribute("cart", cart);
        return "redirect:/orders/panel";
    }

    @PostMapping("/cart/remove")
    public String removeFromCart(@RequestParam Long productId, HttpSession session) {
        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
        if (cart != null) {
            cart.remove(productId);
            session.setAttribute("cart", cart);
        }
        return "redirect:/orders/panel";
    }

    @PostMapping("/favorites/toggle")
    public String toggleFavorite(@RequestParam Long productId, HttpSession session) {
        Set<Long> favorites = (Set<Long>) session.getAttribute("favorites");
        if (favorites == null) {
            favorites = new HashSet<>();
        }
        if (favorites.contains(productId)) {
            favorites.remove(productId);
        } else {
            favorites.add(productId);
        }
        session.setAttribute("favorites", favorites);
        return "redirect:/orders/panel";
    }

    @PostMapping("/favorites/remove")
    public String removeFavorite(@RequestParam Long productId, HttpSession session) {
        Set<Long> favorites = (Set<Long>) session.getAttribute("favorites");
        if (favorites != null) {
            favorites.remove(productId);
            session.setAttribute("favorites", favorites);
        }
        return "redirect:/orders/panel";
    }
}