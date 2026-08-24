package net.myapplication.myapp.object.product.service;

import java.util.List;

import net.myapplication.myapp.object.product.dto.ProductResponseDto;

public interface ProductService {

    List<ProductResponseDto> getAllProducts();

    ProductResponseDto getProductById(Long id);

    List<ProductResponseDto> getProductsByCategory(
            String category
    );
}
