package com.example.StockTrack.service;

import com.example.StockTrack.dtos.IngredientResponseDto;
import com.example.StockTrack.entity.Category;
import com.example.StockTrack.entity.Ingredient;
import com.example.StockTrack.repository.CategoryRepository;
import com.example.StockTrack.repository.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class IngredientService {

    private final IngredientRepository ingredientRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public IngredientService(IngredientRepository ingredientRepository,CategoryRepository categoryRepository) {
        this.ingredientRepository = ingredientRepository;
        this.categoryRepository = categoryRepository;
    }

    public Ingredient saveIngredient(Ingredient ingredient) {
        return ingredientRepository.save(ingredient);
    }

    public List<IngredientResponseDto> getAllIngredients() {
        List<Ingredient> ingredients = ingredientRepository.findAll();
        return ingredients.stream()
                .map(IngredientResponseDto::fromEntity) // Convert each ingredient to DTO
                .collect(Collectors.toList());
    }


    public IngredientResponseDto getIngredientById(UUID id) {
        Ingredient ingredient = ingredientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ingredient not found with id: " + id));
        return IngredientResponseDto.fromEntity(ingredient);
    }


    public IngredientResponseDto updateIngredient(UUID id, Ingredient updatedIngredient) {
        Ingredient existingIngredient = ingredientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ingredient not found with id: " + id));
        // Fetch the new category from DB
        Category newCategory = categoryRepository.findById(updatedIngredient.getCategory().getId())
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + updatedIngredient.getCategory().getId()));

        // Update fields
        existingIngredient.setName(updatedIngredient.getName());
        existingIngredient.setDescription(updatedIngredient.getDescription());
        existingIngredient.setQuantity(updatedIngredient.getQuantity());
        existingIngredient.setUnit(updatedIngredient.getUnit());
        existingIngredient.setCategory(newCategory);
        existingIngredient.setImageUrl(updatedIngredient.getImageUrl());
        existingIngredient.setExpiryDate(updatedIngredient.getExpiryDate());
        existingIngredient.setPrice(updatedIngredient.getPrice());
        existingIngredient.setSupplier(updatedIngredient.getSupplier());

        Ingredient savedIngredient = ingredientRepository.save(existingIngredient);

        // Convert to DTO before returning
        return IngredientResponseDto.fromEntity(savedIngredient);
    }




    public void deleteIngredient(UUID id) {
        if (!ingredientRepository.existsById(id)) {
            throw new RuntimeException("Ingredient not found with id: " + id);
        }
        ingredientRepository.deleteById(id);
    }

    public List<IngredientResponseDto> getLowStockIngredients() {
        return ingredientRepository.findByQuantityLessThan(5)
                .stream()
                .map(IngredientResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<IngredientResponseDto> getIngredientsByCategoryName(String categoryName) {
        List<Ingredient> ingredients = ingredientRepository.findByCategoryName(categoryName);
        return ingredients.stream()
                .map(IngredientResponseDto::fromEntity) // Convert entities to DTOs
                .collect(Collectors.toList());
    }

    public List<IngredientResponseDto> getExpiredIngredients() {
        return ingredientRepository.findByExpiryDateBefore(LocalDate.now())
                .stream()
                .map(IngredientResponseDto::fromEntity)
                .collect(Collectors.toList());
    }


    public List<IngredientResponseDto> searchByName(String name) {
        return ingredientRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(IngredientResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

}
