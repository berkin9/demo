package com.example.demo.controller;

import com.example.demo.entity.Order;
import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PanelController {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @GetMapping("/panel")
    public String panel(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long productId,
            Model model
    ) {
        List<User> users = userRepository.findAll();
        List<Product> products = productRepository.findAll();
        List<Order> orders;

        if (userId != null) {
            orders = orderRepository.findByUser_Id(userId);
        } else if (productId != null) {
            orders = orderRepository.findByProduct_Id(productId);
        } else {
            orders = orderRepository.findAll();
        }

        model.addAttribute("users", users);
        model.addAttribute("products", products);
        model.addAttribute("orders", orders);

        return "panel";
    }
}
