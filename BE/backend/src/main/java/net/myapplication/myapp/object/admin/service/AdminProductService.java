package net.myapplication.myapp.object.admin.service;

import org.springframework.data.domain.Pageable;

import net.myapplication.myapp.object.admin.dto.admin.product.AdminCreateProductRequest;
import net.myapplication.myapp.object.admin.dto.admin.product.AdminUpdateProductRequest;
import net.myapplication.myapp.object.product.dto.PageResponse;
import net.myapplication.myapp.object.product.dto.ProductResponseDto;
import net.myapplication.myapp.object.product.dto.request.ProductFilterRequest;

public interface AdminProductService {

        PageResponse<ProductResponseDto> getProducts(
                        ProductFilterRequest filter,
                        Pageable pageable);

        ProductResponseDto getProductById(Long id);

        ProductResponseDto createProduct(
                        AdminCreateProductRequest request);

        ProductResponseDto updateProduct(
                        Long id,
                        AdminUpdateProductRequest request);

        ProductResponseDto updateProductStatus(
                        Long id,
                        Boolean active);
}
