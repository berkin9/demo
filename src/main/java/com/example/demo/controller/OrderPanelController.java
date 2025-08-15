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

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderPanelController {

    private final OrderService orderService;
    private final ProductRepository productRepository;

    @GetMapping("/panel")
    public String orderPanel(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        model.addAttribute("user", user);
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("orders", orderService.findByUserId(user.getId()));
        return "order-panel";
    }

    @PostMapping("/panel")
    public String placeOrder(@RequestParam Long productId,
                             @RequestParam Integer quantity,
                             HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        orderService.createOrder(user, product, quantity);
        return "redirect:/orders/panel";
    }
}
