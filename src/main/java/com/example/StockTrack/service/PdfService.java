package com.example.StockTrack.service;

import com.example.StockTrack.entity.Ingredient;
import com.example.StockTrack.repository.IngredientRepository;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Paragraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class PdfService {

    private final IngredientRepository ingredientRepository;

    @Autowired
    public PdfService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public byte[] generateIngredientReport() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdfDocument = new PdfDocument(writer);
            Document document = new Document(pdfDocument);

            // Title
            document.add(new Paragraph("StockTrack - Ingredient Report").setBold().setFontSize(18));
            document.add(new Paragraph("Generated On: " + java.time.LocalDateTime.now()).setFontSize(12));

            // Table Setup
            float[] columnWidths = {30f, 100f, 50f, 50f, 80f, 100f, 60f, 80f};
            Table table = new Table(columnWidths);

            // Table Headers
            table.addCell("#");
            table.addCell("Name");
            table.addCell("Quantity");
            table.addCell("Unit");
            table.addCell("Category");
            table.addCell("Expiry Date");
            table.addCell("Price (₹)");
            table.addCell("Supplier");

            // Fetch Data from DB
            List<Ingredient> ingredients = ingredientRepository.findAll();
            int index = 1;

            for (Ingredient ingredient : ingredients) {
                table.addCell(String.valueOf(index++));
                table.addCell(ingredient.getName());
                table.addCell(String.valueOf(ingredient.getQuantity()));
                table.addCell(ingredient.getUnit());
                table.addCell(ingredient.getCategory());
                table.addCell(String.valueOf(ingredient.getExpiryDate()));
                table.addCell("₹" + ingredient.getPrice());
                table.addCell(ingredient.getSupplier());
            }

            document.add(table);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return outputStream.toByteArray();
    }
}
