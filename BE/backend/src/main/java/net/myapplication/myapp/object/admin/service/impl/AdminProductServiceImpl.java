package net.myapplication.myapp.object.admin.service.impl;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import net.myapplication.myapp.object.admin.dto.admin.product.AdminCreateProductRequest;
import net.myapplication.myapp.object.admin.service.AdminProductService;
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

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public PageResponse<ProductResponseDto> getProducts(
            ProductFilterRequest filter,
            Pageable pageable) {

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
                        pageable)
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
    @Transactional
@Override
public ProductResponseDto createProduct(
        AdminCreateProductRequest request
) {

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
                    : 0
    );

    product.setNew(
            Boolean.TRUE.equals(request.getIsNew())
    );

    product.setActive(
            request.getActive() == null
                    || request.getActive()
    );

    product.setRating(BigDecimal.ZERO);
    product.setReviewCount(0);

    Product saved =
            productRepository.save(product);

    return productMapper.toResponseDto(saved);
}
}