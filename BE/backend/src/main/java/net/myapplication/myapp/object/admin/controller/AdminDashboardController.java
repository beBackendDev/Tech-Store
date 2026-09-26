package net.myapplication.myapp.object.admin.controller;

import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import lombok.RequiredArgsConstructor;
import net.myapplication.myapp.common.ApiResponseDTO;
import net.myapplication.myapp.object.admin.dto.admin.product.AdminCreateProductRequest;
import net.myapplication.myapp.object.admin.dto.dashboard.AdminDashboardResponseDto;
import net.myapplication.myapp.object.admin.service.AdminDashboardService;
import net.myapplication.myapp.object.admin.service.AdminProductService;
import net.myapplication.myapp.object.product.dto.PageResponse;
import net.myapplication.myapp.object.product.dto.ProductResponseDto;
import net.myapplication.myapp.object.product.dto.request.ProductFilterRequest;
import net.myapplication.myapp.object.product.service.ProductService;

import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/dashboard/admin")
@RequiredArgsConstructor
// hasAuthority thi DB la ADMIN | hasRole thi DB phai ROLE_ADMIN
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminDashboardController {

        private final AdminDashboardService adminDashboardService;
        private final AdminProductService adminProductService;

        // ================================DASHBOARD========================================
        @GetMapping
        public ResponseEntity<ApiResponseDTO<AdminDashboardResponseDto>> getDashboard() {

                AdminDashboardResponseDto response = adminDashboardService
                                .getDashboard();

                return ResponseEntity.ok(

                                ApiResponseDTO
                                                .<AdminDashboardResponseDto>builder()

                                                .status("SUCCESS")

                                                .message(
                                                                "Dashboard data retrieved successfully")

                                                .response(response)

                                                .build());
        }
        // ================================PRODUCT-MANAGEMENT========================================

        @GetMapping("/products")
        public ResponseEntity<ApiResponseDTO<PageResponse<ProductResponseDto>>> getProducts(

                        @ModelAttribute ProductFilterRequest filter,

                        @PageableDefault(page = 0, size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable

        ) {

                PageResponse<ProductResponseDto> products = adminProductService.getProducts(
                                filter,
                                pageable);

                ApiResponseDTO<PageResponse<ProductResponseDto>> response = ApiResponseDTO
                                .<PageResponse<ProductResponseDto>>builder()
                                .status("SUCCESS")
                                .message("Admin products retrieved successfully")
                                .response(products)
                                .build();

                return ResponseEntity.ok(response);
        }

        // CREATE-PRODUCT
        @PostMapping("/create-product")
        public ResponseEntity<ApiResponseDTO<ProductResponseDto>> createProduct(

                        @Valid @RequestBody AdminCreateProductRequest request

        ) {

                ProductResponseDto product = adminProductService.createProduct(request);

                ApiResponseDTO<ProductResponseDto> response = ApiResponseDTO
                                .<ProductResponseDto>builder()
                                .status("SUCCESS")
                                .message("Product created successfully")
                                .response(product)
                                .build();

                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(response);
        }
        // ================================INVENTORY-MANAGEMENT========================================
        // ================================ORDER-MANAGEMENT========================================
        // ================================CUSTOMER-MANAGEMENT========================================
        // ================================RETURN-MANAGEMENT========================================
        // ================================ANALYTICS========================================

}
