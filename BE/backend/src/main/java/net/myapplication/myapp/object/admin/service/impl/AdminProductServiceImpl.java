package net.myapplication.myapp.object.admin.service.impl;

import java.math.BigDecimal;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import net.myapplication.myapp.object.admin.dto.admin.product.AdminCreateProductRequest;
import net.myapplication.myapp.object.admin.dto.admin.product.AdminUpdateProductRequest;
import net.myapplication.myapp.object.admin.service.AdminProductService;
import net.myapplication.myapp.object.product.constants.ProductSortField;
import net.myapplication.myapp.object.product.dto.PageResponse;
import net.myapplication.myapp.object.product.dto.ProductResponseDto;
import net.myapplication.myapp.object.product.dto.request.ProductFilterRequest;
import net.myapplication.myapp.object.product.entity.Product;
import net.myapplication.myapp.object.product.mapper.ProductMapper;
import net.myapplication.myapp.object.product.repository.ProductRepository;
import net.myapplication.myapp.object.product.specification.ProductSpecification;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminProductServiceImpl
                implements AdminProductService {
        private static final int DEFAULT_PAGE_SIZE = 20;

        private static final int MAX_PAGE_SIZE = 100;

        private final ProductRepository productRepository;

        private final ProductMapper productMapper;

        @Override
        public PageResponse<ProductResponseDto> getProducts(
                        ProductFilterRequest filter,
                        Pageable pageable) {

                validateFilter(filter);
                validatePageable(pageable);

                Pageable safePageable = buildSafePageable(pageable);

                Specification<Product> specification = Specification.allOf(
                                ProductSpecification.keyword(
                                                filter.getKeyword()),
                                ProductSpecification.category(
                                                filter.getCategory()),
                                ProductSpecification.brand(
                                                filter.getBrand()),
                                ProductSpecification.minPrice(
                                                filter.getMinPrice()),
                                ProductSpecification.maxPrice(
                                                filter.getMaxPrice()),
                                ProductSpecification.minRating(
                                                filter.getMinRating()),
                                ProductSpecification.active(
                                                filter.getActive()));

                Page<ProductResponseDto> page = productRepository
                                .findAll(
                                                specification,
                                                safePageable)
                                .map(productMapper::toResponseDto);

                return PageResponse
                                .<ProductResponseDto>builder()
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

        // helper
        private void validateFilter(
                        ProductFilterRequest filter) {

                if (filter.getMinPrice() != null
                                &&
                                filter.getMaxPrice() != null
                                &&
                                filter.getMinPrice()
                                                .compareTo(filter.getMaxPrice()) > 0) {

                        throw new IllegalArgumentException(
                                        "minPrice must not be greater than maxPrice.");
                }

                if (filter.getMinRating() != null
                                &&
                                (filter.getMinRating().compareTo(
                                                java.math.BigDecimal.ZERO) < 0
                                                ||
                                                filter.getMinRating().compareTo(
                                                                java.math.BigDecimal.valueOf(5)) > 0)) {

                        throw new IllegalArgumentException(
                                        "minRating must be between 0 and 5.");
                }
        }

        private void validatePageable(
                        Pageable pageable) {

                if (pageable.getPageNumber() < 0) {

                        throw new IllegalArgumentException(
                                        "Page index must not be negative.");
                }

                if (pageable.getPageSize() <= 0) {

                        throw new IllegalArgumentException(
                                        "Page size must be greater than zero.");
                }

                if (pageable.getPageSize() > MAX_PAGE_SIZE) {

                        throw new IllegalArgumentException(
                                        "Page size must not exceed "
                                                        + MAX_PAGE_SIZE
                                                        + ".");
                }

                pageable.getSort()
                                .forEach(order -> {

                                        String property = order.getProperty();

                                        if (!ProductSortField
                                                        .isAllowed(property)) {

                                                throw new IllegalArgumentException(
                                                                "Sorting by '"
                                                                                + property
                                                                                + "' is not allowed.");
                                        }
                                });
        }

        private Pageable buildSafePageable(
                        Pageable pageable) {

                Sort sort = pageable.getSort();

                if (sort.isUnsorted()) {

                        sort = Sort.by(
                                        Sort.Order.desc("createdAt"));
                }

                return PageRequest.of(
                                pageable.getPageNumber(),
                                Math.min(
                                                pageable.getPageSize(),
                                                MAX_PAGE_SIZE),
                                sort);
        }

        @Override
        @Transactional(readOnly = true)
        public ProductResponseDto getProductById(Long id) {

                Product product = productRepository
                                .findById(id)
                                .orElseThrow(
                                                () -> new RuntimeException(
                                                                "Product not found: " + id));

                return productMapper.toResponseDto(product);
        }

        @Transactional
        @Override
        public ProductResponseDto createProduct(
                        AdminCreateProductRequest request) {

                Product product = new Product();

                product.setName(request.getName());
                product.setDescription(request.getDescription());
                product.setCategory(request.getCategory());
                product.setPrice(request.getPrice());
                product.setOldPrice(request.getOldPrice());
                product.setImage(request.getImage());

                product.setStock(
                                request.getInitialStock() != null
                                                ? request.getInitialStock()
                                                : 0);

                product.setNew(
                                Boolean.TRUE.equals(request.getIsNew()));

                product.setActive(
                                request.getActive() == null
                                                || request.getActive());

                product.setRating(BigDecimal.ZERO);
                product.setReviewCount(0);

                Product saved = productRepository.save(product);

                return productMapper.toResponseDto(saved);
        }

        @Transactional
        @Override
        public ProductResponseDto updateProduct(
                        Long id,
                        AdminUpdateProductRequest request) {

                Product product = productRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException(
                                                "Product not found: " + id));

                if (request.getName() != null) {
                        product.setName(request.getName());
                }

                if (request.getDescription() != null) {
                        product.setDescription(request.getDescription());
                }

                if (request.getCategory() != null) {
                        product.setCategory(request.getCategory());
                }

                if (request.getPrice() != null) {
                        product.setPrice(request.getPrice());
                }

                if (request.getOldPrice() != null) {
                        product.setOldPrice(request.getOldPrice());
                }

                if (request.getImage() != null) {
                        product.setImage(request.getImage());
                }

                if (request.getIsNew() != null) {
                        product.setNew(request.getIsNew());
                }

                return productMapper.toResponseDto(product);
        }

        @Transactional
        @Override
        public ProductResponseDto updateProductStatus(
                        Long id,
                        Boolean active) {

                Product product = productRepository
                                .findById(id)
                                .orElseThrow(
                                                () -> new RuntimeException(
                                                                "Product not found: " + id));

                product.setActive(active);

                return productMapper.toResponseDto(product);
        }
}