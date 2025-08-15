package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {

    private final UserRepository userRepository;

    // Admin bilgisi (sabit)
    private final String ADMIN_EMAIL = "admin@example.com";
    private final String ADMIN_PASSWORD = "admin123";

    @GetMapping
    public String loginForm() {
        return "login";
    }

    @PostMapping
    public String loginSubmit(@RequestParam String email,
                              @RequestParam String password,
                              HttpSession session,
                              Model model) {

        // Admin kontrolü
        if (email.equals(ADMIN_EMAIL) && password.equals(ADMIN_PASSWORD)) {
            session.setAttribute("admin", true); // admin oturumu
            return "redirect:/panel"; // admin paneline yönlendir
        }

        // Normal kullanıcı kontrolü
        User user = userRepository.findByEmail(email).orElse(null);
        if (user != null && user.getPassword().equals(password)) {
            session.setAttribute("user", user);
            return "redirect:/orders/panel"; // kullanıcı paneline yönlendir
        }

        // Hatalı giriş
        model.addAttribute("error", "Invalid email or password");
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
