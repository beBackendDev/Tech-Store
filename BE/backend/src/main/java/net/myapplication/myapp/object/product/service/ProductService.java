package net.myapplication.myapp.object.product.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import net.myapplication.myapp.object.product.dto.request.ProductFilterRequest;
import net.myapplication.myapp.object.product.dto.response.PageResponse;
import net.myapplication.myapp.object.product.dto.response.ProductResponseDto;

public interface ProductService {

        List<ProductResponseDto> getAllProducts();

        PageResponse<ProductResponseDto> getProducts(
                        ProductFilterRequest filter,
                        Pageable pageable); // get with filter and pagination

        ProductResponseDto getProductById(Long id);

        List<ProductResponseDto> getProductsByCategory(
                        String category);
}
