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

import java.util.HashMap;
import java.util.Map;

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

        var orders = orderService.findByUserId(user.getId());
        model.addAttribute("orders", orders);

        if (!orders.isEmpty()) {
            var lastOrder = orders.get(orders.size() - 1);
            var category = lastOrder.getProduct().getCategory();
            var recommendedProducts = productRepository.findByCategoryIdAndIdNot(
                    category.getId(),
                    lastOrder.getProduct().getId()
            );
            model.addAttribute("recommendedProducts", recommendedProducts);
        }

        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
        if (cart == null) {
            cart = new HashMap<>();
        }

        Map<Product, Integer> cartProducts = new HashMap<>();
        for (Map.Entry<Long, Integer> entry : cart.entrySet()) {
            productRepository.findById(entry.getKey())
                    .ifPresent(p -> cartProducts.put(p, entry.getValue()));
        }
        model.addAttribute("cartProducts", cartProducts);

        double cartTotal = cartProducts.entrySet()
                .stream()
                .mapToDouble(e -> e.getKey().getPrice() * e.getValue())
                .sum();
        model.addAttribute("cartTotal", cartTotal);

        return "order-panel";
    }

    @PostMapping("/cart/add")
    public String addToCart(@RequestParam Long productId,
                            @RequestParam Integer quantity,
                            HttpSession session) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
        if (cart == null) {
            cart = new HashMap<>();
        }

        cart.put(productId, cart.getOrDefault(productId, 0) + quantity);
        session.setAttribute("cart", cart);

        return "redirect:/orders/panel";
    }

    @GetMapping("/cart/checkout")
    public String checkoutPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            return "redirect:/orders/panel";
        }
        model.addAttribute("userAddress", user.getAddress());
        Map<Product, Integer> cartProducts = new HashMap<>();
        double total = 0;
        for (Map.Entry<Long, Integer> entry : cart.entrySet()) {
            Product product = productRepository.findById(entry.getKey())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found"));
            cartProducts.put(product, entry.getValue());
            total += product.getPrice() * entry.getValue();
        }

        model.addAttribute("cartProducts", cartProducts);
        model.addAttribute("cartTotal", total);

        return "checkout";
    }

    @PostMapping("/cart/confirm")
    public String confirmPayment(HttpSession session,
                                 @RequestParam("address") String address) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        if (address == null || address.trim().isEmpty()) {
            address = user.getAddress();
        }

        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
        if (cart != null && !cart.isEmpty()) {
            for (Map.Entry<Long, Integer> entry : cart.entrySet()) {
                Product product = productRepository.findById(entry.getKey())
                        .orElseThrow(() -> new IllegalArgumentException("Product not found"));
                orderService.createOrder(user, product, entry.getValue(), address);
            }
            session.removeAttribute("cart");
        }

        return "redirect:/orders/panel";
    }

}
