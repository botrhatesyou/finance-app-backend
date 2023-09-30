package com.example.financeApp.service;

import com.example.financeApp.model.Category;
import com.example.financeApp.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public Category createCategory(Category category, UUID userId) {
        category.setUserId(userId);
        return categoryRepository.save(category);
    }

    public List<Category> getCategories(UUID userId) {
        return categoryRepository.findByUserId(userId);
    }

    public Category updateCategory(Category category, UUID userId) {
        // Retrieve the existing category from the database
        Category existingCategory = categoryRepository.findById(category.getId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        // Check if the existing category's userId matches the userId from the token
        if (existingCategory.getUserId() == null || !existingCategory.getUserId().equals(userId)) {
            return null; // Unauthorized or invalid request
        }

        // Update the existing category
        existingCategory.setName(category.getName());
        return categoryRepository.save(existingCategory);
    }


    public void deleteCategory(UUID categoryId, UUID userId) {
        Category category = categoryRepository.findById(categoryId).orElse(null);
        if (category != null && category.getUserId().equals(userId)) {
            categoryRepository.delete(category);
        }
    }
}
