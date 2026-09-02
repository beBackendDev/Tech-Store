package net.myapplication.myapp.object.product.service;

import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import lombok.RequiredArgsConstructor;

import net.myapplication.myapp.config.DataPipelineProperties;
import net.myapplication.myapp.object.product.dto.dataImportedDto.LaptopSpecificationCsvRow;
import net.myapplication.myapp.object.product.dto.dataImportedDto.ProductCsvRow;
import net.myapplication.myapp.object.product.entity.LaptopSpecification;
import net.myapplication.myapp.object.product.entity.Product;
import net.myapplication.myapp.object.product.repository.LaptopSpecificationRepository;
import net.myapplication.myapp.object.product.repository.ProductRepository;

@Service
@RequiredArgsConstructor
public class ProductImportService {

    private final ProductRepository productRepository;

    private final LaptopSpecificationRepository laptopSpecificationRepository;

    private final DataPipelineProperties properties;

    // =========================================================
    // MAIN IMPORT
    // =========================================================

    @Transactional
    public void importAll() {

        System.out.println(
                "========================================");

        System.out.println(
                "Starting product data import...");

        System.out.println(
                "========================================");

        importProducts();

        importLaptopSpecifications();

        System.out.println(
                "========================================");

        System.out.println(
                "Product data import completed successfully.");

        System.out.println(
                "========================================");
    }

    // =========================================================
    // IMPORT PRODUCTS
    // =========================================================

    private void importProducts() {

        Path csvPath = Paths.get(
                properties.getProductsCsv());

        printCsvInfo(
                "Products",
                csvPath);

        try (
                Reader reader = Files.newBufferedReader(
                        csvPath,
                        StandardCharsets.UTF_8)) {

            CsvToBean<ProductCsvRow> csvToBean =
                    new CsvToBeanBuilder<ProductCsvRow>(
                            reader)
                            .withType(ProductCsvRow.class)
                            .withIgnoreLeadingWhiteSpace(true)
                            .withIgnoreEmptyLine(true)
                            .build();

            List<ProductCsvRow> rows =
                    csvToBean.parse();

            System.out.println(
                    "Total product rows: "
                            + rows.size());

            if (rows.isEmpty()) {

                throw new IllegalStateException(
                        "products.csv is empty");
            }

            // Debug first 5 rows
            printProductPreview(rows);

            List<Product> products =
                    new ArrayList<>(rows.size());

            for (ProductCsvRow row : rows) {

                validateProductRow(row);

                Product product =
                        Product.builder()
                                .externalId(
                                        row.getExternalId())
                                .name(
                                        row.getName())
                                .description(
                                        row.getDescription())
                                .category(
                                        row.getCategory())
                                .price(
                                        row.getPrice())
                                .oldPrice(
                                        row.getOldPrice())
                                .stock(
                                        row.getStock())
                                .image(
                                        row.getImageUrl())
                                .rating(
                                        row.getRating())
                                .reviewCount(
                                        row.getReviewCount())
                                .isNew(
                                        row.getIsNew())
                                .active(
                                        row.getActive())
                                .build();

                products.add(product);
            }

            productRepository.saveAll(products);

            System.out.println(
                    "Imported products: "
                            + products.size());

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to import products from: "
                            + csvPath
                                    .toAbsolutePath()
                                    .normalize(),
                    e);
        }
    }

    // =========================================================
    // IMPORT LAPTOP SPECIFICATIONS
    // =========================================================

    private void importLaptopSpecifications() {

        Path csvPath = Paths.get(
                properties.getLaptopSpecificationsCsv());

        printCsvInfo(
                "Laptop specifications",
                csvPath);

        try (
                Reader reader = Files.newBufferedReader(
                        csvPath,
                        StandardCharsets.UTF_8)) {

            CsvToBean<LaptopSpecificationCsvRow>
                    csvToBean =
                    new CsvToBeanBuilder<LaptopSpecificationCsvRow>(
                            reader)
                            .withType(
                                    LaptopSpecificationCsvRow.class)
                            .withIgnoreLeadingWhiteSpace(true)
                            .withIgnoreEmptyLine(true)
                            .build();

            List<LaptopSpecificationCsvRow> rows =
                    csvToBean.parse();

            System.out.println(
                    "Total laptop specification rows: "
                            + rows.size());

            if (rows.isEmpty()) {

                throw new IllegalStateException(
                        "laptop_specifications.csv is empty");
            }

            List<LaptopSpecification> specifications =
                    new ArrayList<>(rows.size());

            for (LaptopSpecificationCsvRow row : rows) {

                validateLaptopSpecificationRow(row);

                Product product =
                        productRepository
                                .findByExternalId(
                                        row.getExternalId())
                                .orElseThrow(
                                        () -> new IllegalStateException(
                                                "Product not found for externalId: "
                                                        + row.getExternalId()));

                LaptopSpecification specification =
                        LaptopSpecification.builder()
                                .product(product)
                                .brand(
                                        row.getBrand())
                                .processor(
                                        row.getProcessor())
                                .ram(
                                        row.getRam())
                                .ssd(
                                        row.getSsd())
                                .hardDisk(
                                        row.getHardDisk())
                                .operatingSystem(
                                        row.getOperatingSystem())
                                .graphics(
                                        row.getGraphics())
                                .screenSize(
                                        row.getScreenSize())
                                .resolution(
                                        row.getResolution())
                                .build();

                specifications.add(
                        specification);
            }

            laptopSpecificationRepository
                    .saveAll(specifications);

            System.out.println(
                    "Imported laptop specifications: "
                            + specifications.size());

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to import laptop specifications from: "
                            + csvPath
                                    .toAbsolutePath()
                                    .normalize(),
                    e);
        }
    }

    // =========================================================
    // PRODUCT VALIDATION
    // =========================================================

    private void validateProductRow(
            ProductCsvRow row) {

        if (row == null) {

            throw new IllegalArgumentException(
                    "Product row cannot be null");
        }

        if (row.getExternalId() == null
                || row.getExternalId().isBlank()) {

            throw new IllegalArgumentException(
                    "Product externalId cannot be empty");
        }

        if (row.getName() == null
                || row.getName().isBlank()) {

            throw new IllegalArgumentException(
                    "Product name cannot be empty. "
                            + "externalId="
                            + row.getExternalId());
        }

        if (row.getCategory() == null
                || row.getCategory().isBlank()) {

            throw new IllegalArgumentException(
                    "Product category cannot be empty. "
                            + "externalId="
                            + row.getExternalId());
        }

        if (row.getPrice() == null) {

            throw new IllegalArgumentException(
                    "Product price cannot be null. "
                            + "externalId="
                            + row.getExternalId());
        }

        if (row.getStock() == null) {

            throw new IllegalArgumentException(
                    "Product stock cannot be null. "
                            + "externalId="
                            + row.getExternalId());
        }

        if (row.getImageUrl() == null
                || row.getImageUrl().isBlank()) {

            throw new IllegalArgumentException(
                    "Product image URL cannot be empty. "
                            + "externalId="
                            + row.getExternalId());
        }
    }

    // =========================================================
    // LAPTOP SPECIFICATION VALIDATION
    // =========================================================

    private void validateLaptopSpecificationRow(
            LaptopSpecificationCsvRow row) {

        if (row == null) {

            throw new IllegalArgumentException(
                    "Laptop specification row cannot be null");
        }

        if (row.getExternalId() == null
                || row.getExternalId().isBlank()) {

            throw new IllegalArgumentException(
                    "Laptop specification externalId "
                        + "cannot be empty");
        }
    }

    // =========================================================
    // CSV INFORMATION
    // =========================================================

    private void printCsvInfo(
            String name,
            Path csvPath) {

        Path absolutePath =
                csvPath
                        .toAbsolutePath()
                        .normalize();

        System.out.println(
                "----------------------------------------");

        System.out.println(
                name + " CSV");

        System.out.println(
                "Configured path : "
                        + csvPath);

        System.out.println(
                "Resolved path   : "
                        + absolutePath);

        System.out.println(
                "Exists           : "
                        + Files.exists(absolutePath));

        System.out.println(
                "Regular file     : "
                        + Files.isRegularFile(absolutePath));

        System.out.println(
                "----------------------------------------");

        if (!Files.isRegularFile(absolutePath)) {

            throw new IllegalStateException(
                    name
                            + " CSV does not exist or is not a file: "
                            + absolutePath);
        }
    }

    // =========================================================
    // PRODUCT PREVIEW
    // =========================================================

    private void printProductPreview(
            List<ProductCsvRow> rows) {

        int previewSize =
                Math.min(rows.size(), 5);

        System.out.println(
                "========================================");

        System.out.println(
                "PRODUCT CSV PREVIEW");

        System.out.println(
                "========================================");

        for (int i = 0;
                i < previewSize;
                i++) {

            ProductCsvRow row =
                    rows.get(i);

            System.out.println(
                    "Row " + i);

            System.out.println(
                    "externalId  = "
                            + row.getExternalId());

            System.out.println(
                    "name        = "
                            + row.getName());

            System.out.println(
                    "category    = "
                            + row.getCategory());

            System.out.println(
                    "price       = "
                            + row.getPrice());

            System.out.println(
                    "stock       = "
                            + row.getStock());

            System.out.println(
                    "imageUrl    = "
                            + row.getImageUrl());

            System.out.println(
                    "rating      = "
                            + row.getRating());

            System.out.println(
                    "reviewCount = "
                            + row.getReviewCount());

            System.out.println(
                    "isNew       = "
                            + row.getIsNew());

            System.out.println(
                    "active      = "
                            + row.getActive());

            System.out.println(
                    "----------------------------------------");
        }
    }
}