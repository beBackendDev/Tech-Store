package net.myapplication.myapp.object.product.dto.response;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LaptopSpecificationResponseDto {

    private String brand;

    private String processor;

    private Integer ram;

    private Integer ssd;

    private Integer hardDisk;

    private String operatingSystem;

    private String graphics;

    private BigDecimal screenSize;

    private String resolution;
}
