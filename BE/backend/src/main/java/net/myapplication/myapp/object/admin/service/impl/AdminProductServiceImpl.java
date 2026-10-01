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

import net.myapplication.myapp.object.admin.service.AdminProductService;
import net.myapplication.myapp.object.inventory.service.InventoryService;
import net.myapplication.myapp.object.product.constants.ProductSortField;
import net.myapplication.myapp.object.product.dto.request.AdminCreateProductRequest;
import net.myapplication.myapp.object.product.dto.request.AdminLaptopSpecificationRequest;
import net.myapplication.myapp.object.product.dto.request.AdminUpdateProductRequest;
import net.myapplication.myapp.object.product.dto.request.ProductFilterRequest;
import net.myapplication.myapp.object.product.dto.request.UpdateProductStatusRequest;
import net.myapplication.myapp.object.product.dto.response.AdminProductDetailResponseDto;
import net.myapplication.myapp.object.product.dto.response.PageResponse;
import net.myapplication.myapp.object.product.dto.response.ProductResponseDto;
import net.myapplication.myapp.object.product.entity.LaptopSpecification;
import net.myapplication.myapp.object.product.entity.Product;
import net.myapplication.myapp.object.product.mapper.ProductMapper;
import net.myapplication.myapp.object.product.repository.LaptopSpecificationRepository;
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

        private final LaptopSpecificationRepository laptopSpecificationRepository;

        private final InventoryService inventoryService;

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
        public AdminProductDetailResponseDto getProductsById(Long id) {
                Product product = productRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException(
                                                "Product not found with id: " + id));

                return productMapper.toDetailResponseDto(product);
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
        public AdminProductDetailResponseDto createProduct(
                        AdminCreateProductRequest request) {

                Product product = new Product();

                product.setName(request.getName());
                product.setDescription(request.getDescription());
                product.setCategory(request.getCategory());
                product.setPrice(request.getPrice());
                product.setOldPrice(request.getOldPrice());
                product.setImage(request.getImage());

                product.setNew(
                                Boolean.TRUE.equals(request.getIsNew()));

                product.setActive(
                                request.getActive() == null
                                                || request.getActive());

                // product.setStock(
                // request.getInitialStock() == null
                // ? 0
                // : request.getInitialStock());
                product.setStock(0); // Set initial stock to 0
                product.setReservedStock(0); // Set initial reserved stock to 0

                Product savedProduct = productRepository.save(product);

                // ========================================
                // LAPTOP SPECIFICATION
                // ========================================

                if (request.getLaptopSpecification() != null) {

                        AdminLaptopSpecificationRequest specRequest = request.getLaptopSpecification();

                        LaptopSpecification specification = new LaptopSpecification();

                        specification.setProduct(savedProduct);

                        specification.setBrand(
                                        specRequest.getBrand());

                        specification.setProcessor(
                                        specRequest.getProcessor());

                        specification.setRam(
                                        specRequest.getRam());

                        specification.setSsd(
                                        specRequest.getSsd());

                        specification.setHardDisk(
                                        specRequest.getHardDisk());

                        specification.setOperatingSystem(
                                        specRequest.getOperatingSystem());

                        specification.setGraphics(
                                        specRequest.getGraphics());

                        specification.setScreenSize(
                                        specRequest.getScreenSize());

                        specification.setResolution(
                                        specRequest.getResolution());

                        laptopSpecificationRepository.save(
                                        specification);
                }
                // Initial Stock
                Integer initialStock = request.getInitialStock();
                if (initialStock != null && initialStock > 0) {
                        inventoryService.stockIn(
                                        savedProduct.getId(),
                                        initialStock,
                                        "Initial stock when product was created");
                }
                //response 
                return productMapper.toDetailResponseDto(
                                savedProduct);
        }

        @Transactional
        @Override
        public AdminProductDetailResponseDto updateProduct(
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
                // ========================================
                // LAPTOP SPECIFICATION
                // ========================================

                if (request.getLaptopSpecification() != null) {

                        LaptopSpecification specification = laptopSpecificationRepository
                                        .findByProductId(id)
                                        .orElseGet(() -> {

                                                LaptopSpecification newSpec = new LaptopSpecification();

                                                newSpec.setProduct(product);

                                                return newSpec;
                                        });

                        AdminLaptopSpecificationRequest specRequest = request.getLaptopSpecification();

                        if (specRequest.getBrand() != null) {
                                specification.setBrand(
                                                specRequest.getBrand());
                        }

                        if (specRequest.getProcessor() != null) {
                                specification.setProcessor(
                                                specRequest.getProcessor());
                        }

                        if (specRequest.getRam() != null) {
                                specification.setRam(
                                                specRequest.getRam());
                        }

                        if (specRequest.getSsd() != null) {
                                specification.setSsd(
                                                specRequest.getSsd());
                        }

                        if (specRequest.getHardDisk() != null) {
                                specification.setHardDisk(
                                                specRequest.getHardDisk());
                        }

                        if (specRequest.getOperatingSystem() != null) {
                                specification.setOperatingSystem(
                                                specRequest.getOperatingSystem());
                        }

                        if (specRequest.getGraphics() != null) {
                                specification.setGraphics(
                                                specRequest.getGraphics());
                        }

                        if (specRequest.getScreenSize() != null) {
                                specification.setScreenSize(
                                                specRequest.getScreenSize());
                        }

                        if (specRequest.getResolution() != null) {
                                specification.setResolution(
                                                specRequest.getResolution());
                        }

                        laptopSpecificationRepository.save(
                                        specification);
                        product.setLaptopSpecification(specification);
                }
                return productMapper.toDetailResponseDto(product);
        }

        @Override
        @Transactional
        public AdminProductDetailResponseDto updateProductStatus(
                        Long id,
                        UpdateProductStatusRequest request) {

                Product product = productRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException(
                                                "Product not found with id: " + id));

                product.setActive(
                                request.getActive());

                return productMapper.toDetailResponseDto(
                                product);
        }

}