package net.myapplication.myapp.object.admin.service;

import org.springframework.data.domain.Pageable;

import net.myapplication.myapp.object.product.dto.request.AdminCreateProductRequest;
import net.myapplication.myapp.object.product.dto.request.AdminUpdateProductRequest;
import net.myapplication.myapp.object.product.dto.request.ProductFilterRequest;
import net.myapplication.myapp.object.product.dto.request.UpdateProductStatusRequest;
import net.myapplication.myapp.object.product.dto.response.AdminProductDetailResponseDto;
import net.myapplication.myapp.object.product.dto.response.PageResponse;
import net.myapplication.myapp.object.product.dto.response.ProductResponseDto;

public interface AdminProductService {

        PageResponse<ProductResponseDto> getProducts(
                        ProductFilterRequest filter,
                        Pageable pageable);

        ProductResponseDto getProductById(Long id);

        AdminProductDetailResponseDto getProductsById(
                        Long id);//test

        AdminProductDetailResponseDto createProduct(
                        AdminCreateProductRequest request);

        AdminProductDetailResponseDto updateProduct(
                        Long id,
                        AdminUpdateProductRequest request);

        AdminProductDetailResponseDto updateProductStatus(
                        Long id,
                        UpdateProductStatusRequest request);
}
