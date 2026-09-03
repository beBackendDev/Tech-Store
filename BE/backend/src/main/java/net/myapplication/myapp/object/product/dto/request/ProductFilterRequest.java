package net.myapplication.myapp.object.product.dto.request;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductFilterRequest {

    private String keyword;

    private String category;

    private String brand;

    private BigDecimal minPrice;

    private BigDecimal maxPrice;

    private BigDecimal minRating;

    private Boolean active;
}