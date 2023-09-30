package com.example.financeApp.controller;

import com.example.financeApp.model.Category;
import com.example.financeApp.service.CategoryService;
import com.example.financeApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category category, @RequestHeader("Authorization") String token) {
        UUID userId = getUserIdFromToken(token);
        System.out.println("Parsed userId from token: " + userId);
        Category newCategory = categoryService.createCategory(category, userId);
        return ResponseEntity.ok(newCategory);
    }


    @GetMapping
    public ResponseEntity<List<Category>> getCategories(@RequestHeader("Authorization") String token) {
        UUID userId = getUserIdFromToken(token);
        List<Category> categories = categoryService.getCategories(userId);
        return ResponseEntity.ok(categories);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<Category> updateCategory(@PathVariable UUID categoryId, @RequestBody Category category, @RequestHeader("Authorization") String token) {
        UUID userId = getUserIdFromToken(token);
        category.setId(categoryId);
        Category updatedCategory = categoryService.updateCategory(category, userId);
        if (updatedCategory != null) {
            System.out.println("Received Category: " + category);
            System.out.println("Received User ID: " + category.getUserId());
            return ResponseEntity.ok(updatedCategory);
        }
        return ResponseEntity.status(401).body(null);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID categoryId, @RequestHeader("Authorization") String token) {
        UUID userId = getUserIdFromToken(token);
        categoryService.deleteCategory(categoryId, userId);
        return ResponseEntity.ok().build();
    }

    private UUID getUserIdFromToken(String token) {
        // Extract the user ID from the token
        return UserService.getUserIdFromToken(token);
    }
}
