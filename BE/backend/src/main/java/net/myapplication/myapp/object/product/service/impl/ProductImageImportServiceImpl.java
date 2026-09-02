package net.myapplication.myapp.object.product.service.impl;

import java.io.File;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

import lombok.RequiredArgsConstructor;

import net.myapplication.myapp.config.DataPipelineProperties;
import net.myapplication.myapp.object.product.dto.dataImportedDto.ProductCsvRow;
import net.myapplication.myapp.object.product.service.CloudinaryService;

@Service
@RequiredArgsConstructor
public class ProductImageImportServiceImpl {

    private final CloudinaryService cloudinaryService;

    private final DataPipelineProperties properties;

    public void uploadAllImages() {

        System.out.println(
                "Starting product image assignment...");

        // ==========================================
        // 1. READ PRODUCTS CSV
        // ==========================================

        List<ProductCsvRow> rows = readProductsCsv();

        System.out.println(
                "Total products: " + rows.size());

        // ==========================================
        // 2. FIND TEMPLATE IMAGES
        // ==========================================

        List<File> templateImages = findTemplateImages();

        if (templateImages.isEmpty()) {

            throw new RuntimeException(
                    "No template images found in: "
                            + properties.getImageDir());
        }

        System.out.println(
                "Template images found: "
                        + templateImages.size());

        // ==========================================
        // 3. UPLOAD TEMPLATE IMAGES
        // ==========================================

        List<String> imageUrls =
                uploadTemplateImages(templateImages);

        System.out.println(
                "Uploaded image URLs: "
                        + imageUrls.size());

        // ==========================================
        // 4. ASSIGN URL TO PRODUCTS
        // ==========================================

        int assigned = 0;

        for (int i = 0; i < rows.size(); i++) {

            ProductCsvRow row = rows.get(i);

            String imageUrl =
                    imageUrls.get(i % imageUrls.size());

            row.setImageUrl(imageUrl);

            assigned++;

            System.out.println(
                    "ASSIGNED: "
                            + row.getExternalId()
                            + " -> "
                            + imageUrl);
        }

        // ==========================================
        // 5. WRITE CSV
        // ==========================================

        writeProductsCsv(rows);

        // ==========================================
        // RESULT
        // ==========================================

        System.out.println(
                """
                =====================================
                IMAGE IMPORT RESULT
                =====================================
                Products         : %d
                Template images  : %d
                Cloudinary upload: %d
                Assigned URLs    : %d
                =====================================
                """
                        .formatted(
                                rows.size(),
                                templateImages.size(),
                                imageUrls.size(),
                                assigned));
    }

    // =====================================================
    // FIND TEMPLATE IMAGES
    // =====================================================

    private List<File> findTemplateImages() {

        Path imageDirectory = Paths.get(
                properties.getImageDir());

        System.out.println(
                "Image directory: "
                        + imageDirectory
                                .toAbsolutePath()
                                .normalize());

        if (!Files.exists(imageDirectory)) {

            throw new RuntimeException(
                    "Image directory does not exist: "
                            + imageDirectory
                                    .toAbsolutePath()
                                    .normalize());
        }

        if (!Files.isDirectory(imageDirectory)) {

            throw new RuntimeException(
                    "Image path is not a directory: "
                            + imageDirectory
                                    .toAbsolutePath()
                                    .normalize());
        }

        try {

            return Files.list(imageDirectory)
                    .filter(Files::isRegularFile)
                    .filter(this::isImageFile)
                    .map(Path::toFile)
                    .sorted(
                            Comparator.comparing(
                                    File::getName))
                    .toList();

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to read template images",
                    e);
        }
    }

    // =====================================================
    // CHECK IMAGE EXTENSION
    // =====================================================

    private boolean isImageFile(Path path) {

        String fileName =
                path.getFileName()
                        .toString()
                        .toLowerCase();

        return fileName.endsWith(".jpeg")
                || fileName.endsWith(".jpg")
                || fileName.endsWith(".png")
                || fileName.endsWith(".webp");
    }

    // =====================================================
    // UPLOAD TEMPLATE IMAGES
    // =====================================================

    private List<String> uploadTemplateImages(
            List<File> templateImages) {

        List<String> imageUrls =
                new ArrayList<>();

        for (int i = 0;
                i < templateImages.size();
                i++) {

            File imageFile =
                    templateImages.get(i);

            String publicId =
                    String.format(
                            "template-%02d",
                            i + 1);

            try {

                System.out.println(
                        "Uploading template: "
                                + imageFile.getName());

                String imageUrl =
                        cloudinaryService
                                .uploadProductImage(
                                        imageFile,
                                        publicId);

                imageUrls.add(imageUrl);

                System.out.println(
                        "SUCCESS: "
                                + publicId
                                + " -> "
                                + imageUrl);

            } catch (Exception e) {

                throw new RuntimeException(
                        "Failed to upload template image: "
                                + imageFile.getName(),
                        e);
            }
        }

        return imageUrls;
    }

    // =====================================================
    // READ PRODUCTS CSV
    // =====================================================

    private List<ProductCsvRow> readProductsCsv() {

        Path csvPath = Paths.get(
                properties.getProductsCsv());

        System.out.println(
                "Configured CSV path: "
                        + properties.getProductsCsv());

        System.out.println(
                "Resolved CSV path: "
                        + csvPath
                                .toAbsolutePath()
                                .normalize());

        System.out.println(
                "File exists: "
                        + Files.exists(csvPath));

        try (
                Reader reader =
                        Files.newBufferedReader(
                                csvPath,
                                StandardCharsets.UTF_8)) {

            CsvToBean<ProductCsvRow> csvToBean =
                    new CsvToBeanBuilder<ProductCsvRow>(
                            reader)
                            .withType(
                                    ProductCsvRow.class)
                            .withIgnoreLeadingWhiteSpace(true)
                            .withIgnoreEmptyLine(true)
                            .build();

            return csvToBean.parse();

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to read products.csv",
                    e);
        }
    }

    // =====================================================
    // WRITE PRODUCTS CSV
    // =====================================================

    private void writeProductsCsv(
            List<ProductCsvRow> rows) {

        Path csvPath = Paths.get(
                properties.getProductsCsv());

        try (
                Writer writer =
                        Files.newBufferedWriter(
                                csvPath,
                                StandardCharsets.UTF_8)) {

            StatefulBeanToCsv<ProductCsvRow>
                    beanToCsv =
                    new StatefulBeanToCsvBuilder<ProductCsvRow>(
                            writer)
                            .build();

            beanToCsv.write(rows);

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to write products.csv",
                    e);
        }
    }
}