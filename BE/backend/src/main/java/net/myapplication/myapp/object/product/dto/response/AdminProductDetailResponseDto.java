package net.myapplication.myapp.object.product.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminProductDetailResponseDto {

    private Long id;

    private String externalId;

    private String name;

    private String description;

    private String category;

    private BigDecimal price;

    private BigDecimal oldPrice;

    private Integer stock;

    private String image;

    private BigDecimal rating;

    private Integer reviewCount;

    private boolean isNew;

    private boolean active;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LaptopSpecificationResponseDto laptopSpecification;
}