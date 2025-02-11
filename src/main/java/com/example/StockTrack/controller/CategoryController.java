package com.example.StockTrack.controller;

import com.example.StockTrack.dtos.CategoryResponseDto;
import com.example.StockTrack.entity.Category;
import com.example.StockTrack.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @PostMapping
    public ResponseEntity<Category> addCategory(@RequestBody Category category) {
        Category savedCategory = categoryService.addCategory(category);
        return ResponseEntity.status(201).body(savedCategory);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable UUID id, @RequestBody Category updatedCategory) {
        return ResponseEntity.ok(categoryService.updateCategory(id, updatedCategory));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable UUID id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("Category deleted successfully.");
    }
}
