package net.myapplication.myapp.object.product.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDto {

    private Long id;

    private String name;

    private String category;

    private BigDecimal price;

    private BigDecimal oldPrice;

    private Integer discount;

    private BigDecimal rating;

    private Integer reviewCount;

    private String image;

    private Integer stock;

    private boolean isNew;

    private boolean active;
}
