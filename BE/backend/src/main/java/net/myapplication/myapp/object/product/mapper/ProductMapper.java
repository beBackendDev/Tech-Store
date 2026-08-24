package net.myapplication.myapp.object.product.mapper;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import net.myapplication.myapp.object.product.dto.ProductResponseDto;
import net.myapplication.myapp.object.product.entity.Product;

@Component
public class ProductMapper {

    public ProductResponseDto toResponseDto(Product product) {

        return ProductResponseDto.builder()

                .id(product.getId())

                .name(product.getName())

                .category(product.getCategory())

                .price(product.getPrice())

                .oldPrice(product.getOldPrice())

                .discount(
                        calculateDiscount(
                                product.getOldPrice(),
                                product.getPrice()
                        )
                )

                .rating(product.getRating())

                .reviewCount(product.getReviewCount())

                .image(product.getImage())

                .stock(product.getStock())

                .isNew(product.isNew())

                .build();
    }


    private Integer calculateDiscount(
            BigDecimal oldPrice,
            BigDecimal price
    ) {

        if (
                oldPrice == null ||
                price == null ||
                oldPrice.compareTo(BigDecimal.ZERO) <= 0 ||
                price.compareTo(oldPrice) >= 0
        ) {
            return 0;
        }

        return oldPrice
                .subtract(price)
                .multiply(BigDecimal.valueOf(100))
                .divide(
                        oldPrice,
                        0,
                        java.math.RoundingMode.HALF_UP
                )
                .intValue();
    }
}
