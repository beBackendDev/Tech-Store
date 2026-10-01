package net.myapplication.myapp.object.inventory.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.myapplication.myapp.common.ApiResponseDTO;
import net.myapplication.myapp.object.inventory.dto.request.InventoryAdjustmentRequest;
import net.myapplication.myapp.object.inventory.dto.request.StockInRequest;
import net.myapplication.myapp.object.inventory.dto.request.StockOutRequest;
import net.myapplication.myapp.object.inventory.dto.response.InventoryResponseDto;
import net.myapplication.myapp.object.inventory.dto.response.InventoryTransactionResponseDto;
import net.myapplication.myapp.object.inventory.service.InventoryService;

@RestController 
@RequestMapping ("/api/admin/inventory")
@RequiredArgsConstructor 
public class InventoryController {

    private final InventoryService inventoryService;

    // =========================================================
    // INVENTORY LIST
    // =========================================================

    @GetMapping 
    public ResponseEntity<
            ApiResponseDTO<Page<InventoryResponseDto>>
        > getInventories(

            @PageableDefault (
                size = 20,
                sort = "name",
                direction = Sort.Direction.ASC
            )
            Pageable pageable
    ) {

        Page<InventoryResponseDto> page =
                inventoryService.getInventories(
                    pageable
                );

        return ResponseEntity.ok(
            ApiResponseDTO
                .<Page<InventoryResponseDto>>builder()
                .status("SUCCESS")
                .message(
                    "Inventory retrieved successfully"
                )
                .response(page)
                .build()
        );
    }

    // =========================================================
    // SINGLE INVENTORY
    // =========================================================

    @GetMapping("/{productId}")
    public ResponseEntity<
            ApiResponseDTO<InventoryResponseDto>
        > getInventory(
            @PathVariable Long productId
    ) {

        InventoryResponseDto inventory =
                inventoryService.getInventory(
                    productId
                );

        return ResponseEntity.ok(
            ApiResponseDTO
                .<InventoryResponseDto>builder()
                .status("SUCCESS")
                .message(
                    "Inventory retrieved successfully"
                )
                .response(inventory)
                .build()
        );
    }

    // =========================================================
    // STOCK IN
    // =========================================================

    @PostMapping ("/{productId}/stock-in")
    public ResponseEntity<ApiResponseDTO<Void>> stockIn(

            @PathVariable Long productId,

            @Valid 
            @RequestBody 
            StockInRequest request
    ) {

        inventoryService.stockIn(
            productId,
            request.getQuantity(),
            request.getNote()
        );

        return ResponseEntity.ok(
            ApiResponseDTO.<Void>builder()
                .status("SUCCESS")
                .message(
                    "Stock added successfully"
                )
                .build()
        );
    }

    // =========================================================
    // STOCK OUT
    // =========================================================

    @PostMapping("/{productId}/stock-out")
    public ResponseEntity<ApiResponseDTO<Void>> stockOut(

            @PathVariable Long productId,

            @Valid
            @RequestBody
            StockOutRequest request
    ) {

        inventoryService.stockOut(
            productId,
            request.getQuantity(),
            request.getNote()
        );

        return ResponseEntity.ok(
            ApiResponseDTO.<Void>builder()
                .status("SUCCESS")
                .message(
                    "Stock removed successfully"
                )
                .build()
        );
    }

    // =========================================================
    // ADJUST
    // =========================================================

    @PostMapping("/{productId}/adjust")
    public ResponseEntity<ApiResponseDTO<Void>> adjustStock(

            @PathVariable Long productId,

            @Valid
            @RequestBody
            InventoryAdjustmentRequest request
    ) {

        inventoryService.adjustStock(
            productId,
            request.getActualStock(),
            request.getNote()
        );

        return ResponseEntity.ok(
            ApiResponseDTO.<Void>builder()
                .status("SUCCESS")
                .message(
                    "Inventory adjusted successfully"
                )
                .build()
        );
    }

    // =========================================================
    // HISTORY OF PRODUCT
    // =========================================================

    @GetMapping("/{productId}/history")
    public ResponseEntity<
            ApiResponseDTO<
                Page<InventoryTransactionResponseDto>
            >
        > getHistory(

            @PathVariable Long productId,

            @PageableDefault(
                size = 20,
                sort = "createdAt",
                direction = Sort.Direction.DESC
            )
            Pageable pageable
    ) {

        Page<InventoryTransactionResponseDto> history =
                inventoryService.getHistory(
                    productId,
                    pageable
                );

        return ResponseEntity.ok(
            ApiResponseDTO
                .<Page<InventoryTransactionResponseDto>>
                builder()
                .status("SUCCESS")
                .message(
                    "Inventory history retrieved successfully"
                )
                .response(history)
                .build()
        );
    }
}
