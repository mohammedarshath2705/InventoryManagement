package com.example.StockTrack.repository;

import com.example.StockTrack.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, UUID> {

    List<Ingredient> findByQuantityLessThan(int quantity);

    List<Ingredient> findByExpiryDateBefore(LocalDate expiryDate);

    @Query("SELECT i FROM Ingredient i WHERE LOWER(i.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Ingredient> findByNameContainingIgnoreCase(@Param("name") String name);

    @Query("SELECT i FROM Ingredient i JOIN i.category c WHERE LOWER(c.name) = LOWER(:categoryName)")
    List<Ingredient> findByCategoryName(@Param("categoryName") String categoryName);

}
