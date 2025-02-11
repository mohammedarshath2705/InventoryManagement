package com.example.StockTrack.service;

import com.example.StockTrack.dtos.CategoryResponseDto;
import com.example.StockTrack.entity.Category;
import com.example.StockTrack.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryResponseDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(CategoryResponseDto::new)  // Convert to DTO
                .collect(Collectors.toList());
    }

    public Category getCategoryById(UUID id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
    }

    public Category addCategory(Category category) {
        if (categoryRepository.findByNameIgnoreCase(category.getName()).isPresent()) {
            throw new RuntimeException("Category with this name already exists.");
        }
        return categoryRepository.save(category);
    }

    public Category updateCategory(UUID id, Category updatedCategory) {
        Category existingCategory = getCategoryById(id);
        existingCategory.setName(updatedCategory.getName());
        return categoryRepository.save(existingCategory);
    }

    public void deleteCategory(UUID id) {
        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("Category not found with id: " + id);
        }
        categoryRepository.deleteById(id);
    }
}
