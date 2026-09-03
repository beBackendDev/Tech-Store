package net.myapplication.myapp.object.product.controller;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import net.myapplication.myapp.common.ApiResponseDTO;
import net.myapplication.myapp.object.product.dto.PageResponse;
import net.myapplication.myapp.object.product.dto.ProductResponseDto;
import net.myapplication.myapp.object.product.dto.request.ProductFilterRequest;
import net.myapplication.myapp.object.product.service.ProductService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductController {
        private final ProductService productService;

        @GetMapping("/public/products")
        public ResponseEntity<List<ProductResponseDto>> getProducts() {

                return ResponseEntity.ok(
                                productService.getAllProducts());
        }

        @GetMapping("/public/products-list")
        public ResponseEntity<ApiResponseDTO<PageResponse<ProductResponseDto>>> getProducts(
                        @ModelAttribute ProductFilterRequest filter,
                        @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable

        ) {
                PageResponse<ProductResponseDto> pageResponse = productService.getProducts(filter, pageable);
                return ResponseEntity.ok(
                                ApiResponseDTO.<PageResponse<ProductResponseDto>>builder()
                                                .status("SUCCESS")
                                                .message("Products retrieved successfully")
                                                .response(pageResponse)
                                                .build());
        }

        @GetMapping("/public/products/{id}")
        public ResponseEntity<ProductResponseDto> getProduct(
                        @PathVariable Long id) {

                return ResponseEntity.ok(
                                productService.getProductById(id));
        }

        @GetMapping("/public/products/category/{category}")
        public ResponseEntity<List<ProductResponseDto>> getByCategory(
                        @PathVariable String category) {

                return ResponseEntity.ok(
                                productService.getProductsByCategory(category));
        }
}
