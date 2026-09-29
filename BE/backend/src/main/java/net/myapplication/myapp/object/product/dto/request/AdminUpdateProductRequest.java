package net.myapplication.myapp.object.product.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class AdminUpdateProductRequest {

    @NotBlank
    @Size(max = 255)
    private String name;

    @Size(max = 2000)
    private String description;

    @NotBlank
    @Size(max = 100)
    private String category;

    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal price;

    @DecimalMin(value = "0.0")
    private BigDecimal oldPrice;

    @Size(max = 1000)
    private String image;

    private Boolean isNew;

    private AdminLaptopSpecificationRequest laptopSpecification;
}
