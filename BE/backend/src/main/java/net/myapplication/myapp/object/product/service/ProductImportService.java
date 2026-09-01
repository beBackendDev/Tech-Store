package net.myapplication.myapp.object.product.service;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import lombok.RequiredArgsConstructor;

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

        // =========================================================
        // MAIN IMPORT
        // =========================================================

        @Transactional
        public void importAll() {

                System.out.println(
                                "Starting product data import...");

                importProducts();

                importLaptopSpecifications();

                System.out.println(
                                "Product data import completed successfully.");
        }

        // =========================================================
        // IMPORT PRODUCTS
        // =========================================================

        private void importProducts() {

                try (
                                Reader reader = new InputStreamReader(
                                                new ClassPathResource(
                                                                "data/processed/products.csv").getInputStream(),
                                                StandardCharsets.UTF_8)) {

                        CsvToBean<ProductCsvRow> csvToBean = new CsvToBeanBuilder<ProductCsvRow>(reader)
                                        .withType(ProductCsvRow.class)
                                        .withIgnoreLeadingWhiteSpace(true)
                                        .withIgnoreEmptyLine(true)
                                        .build();

                        List<ProductCsvRow> rows = csvToBean.parse();

                        System.out.println("=================================");
                        System.out.println("Total rows: " + rows.size());
                        System.out.println("=================================");

                        for (int i = 0; i < Math.min(rows.size(), 5); i++) {

                                ProductCsvRow row = rows.get(i);

                                System.out.println("Row " + i);
                                System.out.println("externalId = " + row.getExternalId());
                                System.out.println("name       = " + row.getName());
                                System.out.println("description = " + row.getDescription());
                                System.out.println("category   = " + row.getCategory());
                                System.out.println("price      = " + row.getPrice());
                                System.out.println("oldPrice   = " + row.getOldPrice());
                                System.out.println("stock      = " + row.getStock());
                                System.out.println("image      = " + row.getImageUrl());
                                System.out.println("rating     = " + row.getRating());
                                System.out.println("reviewCount = " + row.getReviewCount());
                                System.out.println("isNew      = " + row.getIsNew());
                                System.out.println("active     = " + row.getActive());

                                System.out.println("---------------------------------");
                        }

                        List<Product> products = new ArrayList<>();
                        for (ProductCsvRow row : rows) {

                                validateProductRow(row);

                                Product product = Product.builder()
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
                                                // .image(
                                                // row.getImage()
                                                // )
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
                                        "Failed to import products",
                                        e);
                }
        }

        // =========================================================
        // IMPORT LAPTOP SPECIFICATIONS
        // =========================================================

        private void importLaptopSpecifications() {

                try (
                                Reader reader = new InputStreamReader(
                                                new ClassPathResource(
                                                                "data/processed/laptop_specifications.csv")
                                                                .getInputStream(),
                                                StandardCharsets.UTF_8)) {

                        CsvToBean<LaptopSpecificationCsvRow> csvToBean = new CsvToBeanBuilder<LaptopSpecificationCsvRow>(
                                        reader)
                                        .withType(
                                                        LaptopSpecificationCsvRow.class)
                                        .withIgnoreLeadingWhiteSpace(true)
                                        .withIgnoreEmptyLine(true)
                                        .build();

                        List<LaptopSpecificationCsvRow> rows = csvToBean.parse();

                        List<LaptopSpecification> specifications = new ArrayList<>();

                        for (LaptopSpecificationCsvRow row : rows) {

                                Product product = productRepository
                                                .findByExternalId(
                                                                row.getExternalId())
                                                .orElseThrow(() -> new RuntimeException(
                                                                "Product not found for externalId: "
                                                                                + row.getExternalId()));

                                LaptopSpecification specification = LaptopSpecification.builder()
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
                                        "Failed to import laptop specifications",
                                        e);
                }
        }

        // =========================================================
        // VALIDATION
        // =========================================================

        private void validateProductRow(
                        ProductCsvRow row) {

                if (row.getExternalId() == null ||
                                row.getExternalId().isBlank()) {

                        throw new IllegalArgumentException(
                                        "Product externalId cannot be empty");
                }

                if (row.getName() == null ||
                                row.getName().isBlank()) {

                        throw new IllegalArgumentException(
                                        "Product name cannot be empty. externalId="
                                                        + row.getExternalId());
                }

                if (row.getPrice() == null) {

                        throw new IllegalArgumentException(
                                        "Product price cannot be null. externalId="
                                                        + row.getExternalId());
                }

                if (row.getStock() == null) {

                        throw new IllegalArgumentException(
                                        "Product stock cannot be null. externalId="
                                                        + row.getExternalId());
                }
        }
}
