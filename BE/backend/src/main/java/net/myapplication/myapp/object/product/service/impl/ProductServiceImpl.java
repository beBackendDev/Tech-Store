package net.myapplication.myapp.object.product.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.myapplication.myapp.object.product.dto.PageResponse;
import net.myapplication.myapp.object.product.dto.ProductResponseDto;
import net.myapplication.myapp.object.product.dto.request.ProductFilterRequest;
import net.myapplication.myapp.object.product.entity.Product;
import net.myapplication.myapp.object.product.mapper.ProductMapper;
import net.myapplication.myapp.object.product.repository.ProductRepository;
import net.myapplication.myapp.object.product.service.ProductService;
import net.myapplication.myapp.object.product.specification.ProductSpecification;

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
                                                                "Product not found with id: " + id));

                if (!product.isActive()) {
                        throw new RuntimeException(
                                        "Product is not available");
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

        @Override
        public PageResponse<ProductResponseDto> getProducts(ProductFilterRequest filter, Pageable pageable) {
                Specification<Product> specification = Specification
                                .where(ProductSpecification.active(filter.getActive()))
                                .and(ProductSpecification.keyword(filter.getKeyword()))
                                .and(ProductSpecification.category(filter.getCategory()))
                                .and(ProductSpecification.brand(filter.getBrand()))
                                .and(ProductSpecification.minPrice(filter.getMinPrice()))
                                .and(ProductSpecification.maxPrice(filter.getMaxPrice()))
                                .and(ProductSpecification.minRating(filter.getMinRating()));

                Page<ProductResponseDto> page = productRepository
                                .findAll(specification, pageable)
                                .map(productMapper::toResponseDto);

                return PageResponse.<ProductResponseDto>builder()
                                .content(page.getContent())
                                .page(page.getNumber())
                                .size(page.getSize())
                                .numberOfElements(page.getNumberOfElements())
                                .totalElements(page.getTotalElements())
                                .totalPages(page.getTotalPages())
                                .first(page.isFirst())
                                .last(page.isLast())
                                .build();
        }

}
