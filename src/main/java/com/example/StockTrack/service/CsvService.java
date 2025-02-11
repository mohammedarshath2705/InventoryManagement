package com.example.StockTrack.service;

import com.example.StockTrack.entity.Ingredient;
import com.example.StockTrack.repository.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class CsvService {
    private final IngredientRepository ingredientRepository;

    @Autowired
    public CsvService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }
    public byte[] generateIngredientCSV() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try (OutputStreamWriter writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                     .withHeader("ID", "Name", "Quantity", "Unit", "Category", "Expiry Date", "Price", "Supplier"))) {

            // Fetch Ingredients from DB
            List<Ingredient> ingredients = ingredientRepository.findAll();
            int index = 1;

            for (Ingredient ingredient : ingredients) {
                csvPrinter.printRecord(
                        index++,
                        ingredient.getName(),
                        ingredient.getQuantity(),
                        ingredient.getUnit(),
                        ingredient.getCategory(),
                        ingredient.getExpiryDate(),
                        "₹" + ingredient.getPrice(),
                        ingredient.getSupplier()
                );
            }
            csvPrinter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return outputStream.toByteArray();
    }
}
