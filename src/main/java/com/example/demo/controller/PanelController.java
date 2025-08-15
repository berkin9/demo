package com.example.demo.controller;

import com.example.demo.entity.Category;
import com.example.demo.entity.Order;
import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class PanelController {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @GetMapping("/panel")
    public String panel(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long productId,
            Model model,
            HttpSession session
    ) {

        Boolean isAdmin = (Boolean) session.getAttribute("admin");
        if (isAdmin == null || !isAdmin) {
            return "redirect:/login";
        }
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
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryRepository.findAll());

        return "panel";
    }

    @PostMapping("/panel/addProduct")
    public String addProduct(@ModelAttribute Product product,
                             @RequestParam Long categoryId,
                             @RequestParam("image") MultipartFile imageFile,
                             HttpSession session) throws IOException {

        Boolean isAdmin = (Boolean) session.getAttribute("admin");
        if (isAdmin == null || !isAdmin) {
            return "redirect:/login";
        }
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        product.setCategory(category);

        if (!imageFile.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
            Path uploadPath = Paths.get("uploads");
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            Files.copy(imageFile.getInputStream(), uploadPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
            product.setImagePath("/uploads/" + fileName);
        }

        productRepository.save(product);
        return "redirect:/panel";
    }

}
