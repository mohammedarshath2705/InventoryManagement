package com.example.StockTrack.dtos;

import com.example.StockTrack.entity.Ingredient;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.LocalDate;
import java.util.UUID;

@JsonSerialize

public class IngredientResponseDto {
    private UUID id;
    private String name;
    private String description;
    private int quantity;
    private String unit;
    private String category; // Return category NAME instead of ID
    private String imageUrl;
    private LocalDate expiryDate; // Ensure correct type
    private double price;
    private String supplier;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    // ✅ Add Constructor
    public IngredientResponseDto(UUID id, String name, String description, int quantity, String unit, String category,
                                 String imageUrl, LocalDate expiryDate, double price, String supplier) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.unit = unit;
        this.category = category;
        this.imageUrl = imageUrl;
        this.expiryDate = expiryDate;
        this.price = price;
        this.supplier = supplier;
    }

    // Convert Entity to DTO
    public static IngredientResponseDto fromEntity(Ingredient ingredient) {
        return new IngredientResponseDto(
                ingredient.getId(),
                ingredient.getName(),
                ingredient.getDescription(),
                ingredient.getQuantity(),
                ingredient.getUnit(),
                ingredient.getCategory().getName(), // Convert category entity to string
                ingredient.getImageUrl(),
                ingredient.getExpiryDate(), // LocalDate type
                ingredient.getPrice(),
                ingredient.getSupplier()
        );
    }
}
