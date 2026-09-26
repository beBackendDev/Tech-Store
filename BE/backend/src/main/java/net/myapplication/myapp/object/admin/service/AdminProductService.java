package net.myapplication.myapp.object.admin.service;

import org.springframework.data.domain.Pageable;

import net.myapplication.myapp.object.admin.dto.admin.product.AdminCreateProductRequest;
import net.myapplication.myapp.object.product.dto.PageResponse;
import net.myapplication.myapp.object.product.dto.ProductResponseDto;
import net.myapplication.myapp.object.product.dto.request.ProductFilterRequest;

public interface AdminProductService {

    PageResponse<ProductResponseDto> getProducts(
            ProductFilterRequest filter,
            Pageable pageable);

    ProductResponseDto createProduct(
            AdminCreateProductRequest request);
}
