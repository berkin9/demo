package com.example.demo.controller;

import com.example.demo.entity.Category;
import com.example.demo.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryRepository categoryRepository;

    // ==========================
    // REST / Admin Panel GET
    // ==========================
    @GetMapping
    public String listCategories(Model model) {
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
        return "admin/categories"; // Thymeleaf template: admin/categories.html
    }

    // ==========================
    // REST API - Add Category
    // ==========================
    @PostMapping("/add")
    @ResponseBody
    public Category addCategory(@RequestParam String name) {
        Category category = Category.builder().name(name).build();
        return categoryRepository.save(category);
    }

    // ==========================
    // REST API - Update Category
    // ==========================
    @PostMapping("/update/{id}")
    @ResponseBody
    public Category updateCategory(@PathVariable Long id,
                                   @RequestParam String name) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        category.setName(name);
        return categoryRepository.save(category);
    }

    // ==========================
    // REST API - Delete Category
    // ==========================
    @PostMapping("/delete/{id}")
    @ResponseBody
    public String deleteCategory(@PathVariable Long id) {
        categoryRepository.deleteById(id);
        return "Deleted category with id: " + id;
    }

    // ==========================
    // Get category by id (REST)
    // ==========================
    @GetMapping("/{id}")
    @ResponseBody
    public Category getCategory(@PathVariable Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }
}
