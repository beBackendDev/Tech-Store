package net.myapplication.myapp.object.product.dto.request;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class AdminLaptopSpecificationRequest {

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
