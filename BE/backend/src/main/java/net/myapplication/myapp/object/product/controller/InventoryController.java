package net.myapplication.myapp.object.product.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.myapplication.myapp.common.ApiResponseDTO;
import net.myapplication.myapp.object.inventory.service.InventoryService;
import net.myapplication.myapp.object.product.dto.request.AdjustStockRequest;
import net.myapplication.myapp.object.product.dto.request.StockInRequest;

@RestController 
@RequestMapping ("/api/admin/inventory")
@RequiredArgsConstructor 
@PreAuthorize ("hasAuthority('ADMIN')")
public class InventoryController {

    private final InventoryService inventoryService;


    // =========================================================
    // STOCK IN
    // =========================================================

    @PostMapping ("/{productId}/stock-in")
    public ResponseEntity<ApiResponseDTO<Void>>
    stockIn(

            @PathVariable Long productId,

            @Valid 
            @RequestBody 
            StockInRequest request) {

        inventoryService.stockIn(

                productId,

                request.getQuantity(),

                request.getNote()
        );

        return ResponseEntity.ok(

                ApiResponseDTO
                        .<Void>builder()

                        .status("SUCCESS")

                        .message(
                                "Stock added successfully"
                        )

                        .build()
        );
    }


    // =========================================================
    // ADJUST STOCK
    // =========================================================

    @PatchMapping ("/{productId}/adjust")
    public ResponseEntity<ApiResponseDTO<Void>>
    adjustStock(

            @PathVariable Long productId,

            @Valid
            @RequestBody
            AdjustStockRequest request) {

        inventoryService.adjustStock(

                productId,

                request.getQuantity(),

                request.getNote()
        );

        return ResponseEntity.ok(

                ApiResponseDTO
                        .<Void>builder()

                        .status("SUCCESS")

                        .message(
                                "Stock adjusted successfully"
                        )

                        .build()
        );
    }
}
