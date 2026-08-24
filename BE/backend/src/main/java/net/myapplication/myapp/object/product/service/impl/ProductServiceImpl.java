package net.myapplication.myapp.object.product.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.myapplication.myapp.object.product.dto.ProductResponseDto;
import net.myapplication.myapp.object.product.entity.Product;
import net.myapplication.myapp.object.product.mapper.ProductMapper;
import net.myapplication.myapp.object.product.repository.ProductRepository;
import net.myapplication.myapp.object.product.service.ProductService;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl
        implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    @Override
    public List<ProductResponseDto> getAllProducts() {
        return productRepository
                .findByActiveTrue()
                .stream()
                .map(productMapper::toResponseDto)
                .toList();
    }

    @Override
    public ProductResponseDto getProductById(Long id) {
               Product product = productRepository
                .findById(id)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Product not found with id: " + id
                        )
                );

        if (!product.isActive()) {
            throw new RuntimeException(
                    "Product is not available"
            );
        }

        return productMapper.toResponseDto(product);
    }

    @Override
    public List<ProductResponseDto> getProductsByCategory(String category) {
         return productRepository
                .findByCategoryAndActiveTrue(category)
                .stream()
                .map(productMapper::toResponseDto)
                .toList();
    }


}
