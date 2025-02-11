package com.example.StockTrack.controller;

import com.example.StockTrack.dtos.IngredientResponseDto;
import com.example.StockTrack.entity.Category;
import com.example.StockTrack.entity.Ingredient;
import com.example.StockTrack.service.CsvService;
import com.example.StockTrack.service.IngredientService;
import com.example.StockTrack.service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/ingredients")
public class IngredientController {

    private final IngredientService ingredientService;
    private final PdfService pdfService;
    private final CsvService csvService;

    @Autowired
    public IngredientController(IngredientService ingredientService,PdfService pdfService,CsvService csvService) {
        this.ingredientService = ingredientService;
        this.pdfService= pdfService;
        this.csvService = csvService;
    }

    @PostMapping
    public ResponseEntity<Ingredient> addIngredient(@RequestBody Ingredient ingredient) {
        Ingredient savedIngredient = ingredientService.saveIngredient(ingredient);
        return ResponseEntity.status(201).body(savedIngredient);
    }

    @GetMapping
    public ResponseEntity<List<IngredientResponseDto>> getAllIngredients() {
        List<IngredientResponseDto> ingredients = ingredientService.getAllIngredients();
        return ResponseEntity.ok(ingredients);
    }


    @GetMapping("/{id}")
    public ResponseEntity<IngredientResponseDto> getIngredientById(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(ingredientService.getIngredientById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<IngredientResponseDto> updateIngredient(@PathVariable UUID id, @RequestBody Ingredient updatedIngredient) {
        try {
            IngredientResponseDto updated = ingredientService.updateIngredient(id, updatedIngredient);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteIngredient(@PathVariable UUID id) {
        try {
            ingredientService.deleteIngredient(id);
            return ResponseEntity.ok("Ingredient deleted successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<IngredientResponseDto>> getLowStockIngredients() {
        List<IngredientResponseDto> lowStockIngredients = ingredientService.getLowStockIngredients();
        return ResponseEntity.ok(lowStockIngredients);
    }

    @GetMapping("/expired")
    public ResponseEntity<List<IngredientResponseDto>> getExpiredIngredients() {
        List<IngredientResponseDto> expiredIngredients = ingredientService.getExpiredIngredients();
        return ResponseEntity.ok(expiredIngredients);
    }

    @GetMapping("/search")
    public ResponseEntity<List<IngredientResponseDto>> searchIngredients(@RequestParam String name) {
        List<IngredientResponseDto> searchResults = ingredientService.searchByName(name);
        return ResponseEntity.ok(searchResults);
    }

    @GetMapping("/category")
    public ResponseEntity<List<IngredientResponseDto>> getByCategoryName(@RequestParam String name) {
        List<IngredientResponseDto> ingredients = ingredientService.getIngredientsByCategoryName(name);
        return ResponseEntity.ok(ingredients);
    }

    @GetMapping("/reports/pdf")
    public ResponseEntity<byte[]> generatePdfReport() {
        byte[] pdfBytes = pdfService.generateIngredientReport();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ingredients_report.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

    @GetMapping("/export/csv")
    public ResponseEntity<byte[]> generateCsvReport() {
        byte[] csvBytes = csvService.generateIngredientCSV();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ingredients_report.csv")
                .contentType(MediaType.TEXT_PLAIN)
                .body(csvBytes);
    }
}
