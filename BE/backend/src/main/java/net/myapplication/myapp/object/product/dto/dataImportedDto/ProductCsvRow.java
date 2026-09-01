package net.myapplication.myapp.object.product.dto.dataImportedDto;

import java.math.BigDecimal;

import com.opencsv.bean.CsvBindByName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductCsvRow {
    @CsvBindByName(column = "external_id")
    private String externalId;

    @CsvBindByName(column = "name")
    private String name;

    @CsvBindByName(column = "description")
    private String description;

    @CsvBindByName(column = "category")
    private String category;

    @CsvBindByName(column = "price")
    private BigDecimal price;

    @CsvBindByName(column = "old_price")
    private BigDecimal oldPrice;

    @CsvBindByName(column = "stock")
    private Integer stock;

    @CsvBindByName(column = "image_url")
    private String imageUrl;

    @CsvBindByName(column = "rating")
    private BigDecimal rating;

    @CsvBindByName(column = "review_count")
    private Integer reviewCount;

    @CsvBindByName(column = "is_new")
    private Boolean isNew;

    @CsvBindByName(column = "active")
    private Boolean active;
}