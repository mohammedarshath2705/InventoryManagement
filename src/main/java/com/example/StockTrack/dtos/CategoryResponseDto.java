package com.example.StockTrack.dtos;

import com.example.StockTrack.entity.Category;

public class CategoryResponseDto {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Constructor to map from entity to DTO
    public CategoryResponseDto(Category category) {
        this.name = category.getName();
    }
}
