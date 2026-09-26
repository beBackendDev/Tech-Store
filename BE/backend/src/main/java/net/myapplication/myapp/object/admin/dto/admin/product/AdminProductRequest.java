package net.myapplication.myapp.object.admin.dto.admin.product;

import jakarta.validation.constraints.*;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AdminProductRequest {

    @NotBlank(message = "Product name is required")
    @Size(max = 255, message = "Product name must not exceed 255 characters")
    private String name;

    @Size(max = 2000, message = "Description must not exceed 2000 characters")
    private String description;

    @NotBlank(message = "Category is required")
    private String category;

    @NotNull(message = "Price is required")
    @DecimalMin(
        value = "0.0",
        inclusive = false,
        message = "Price must be greater than 0"
    )
    private BigDecimal price;

    @DecimalMin(
        value = "0.0",
        inclusive = true,
        message = "Old price must not be negative"
    )
    private BigDecimal oldPrice;

    @NotNull(message = "Stock is required")
    @Min(
        value = 0,
        message = "Stock must not be negative"
    )
    private Integer stock;

    private String image;

    @DecimalMin(
        value = "0.0",
        inclusive = true,
        message = "Rating must not be negative"
    )
    @DecimalMax(
        value = "5.0",
        inclusive = true,
        message = "Rating must not exceed 5"
    )
    private BigDecimal rating;

    @Min(0)
    private Integer reviewCount;

    private Boolean isNew;

    private Boolean active;
}
