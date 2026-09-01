package net.myapplication.myapp.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import net.myapplication.myapp.object.product.service.ProductImportService;

@RestController
@RequestMapping("/api/dashboard/admin/import")
@RequiredArgsConstructor
public class AdminController {
    private final ProductImportService productImportService;

    @PostMapping("/products")
    public ResponseEntity<String> importProducts() {

        productImportService.importAll();

        return ResponseEntity.ok(
                "Product import completed successfully.");
    }
}
