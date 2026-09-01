package net.myapplication.myapp.object.product.dto.dataImportedDto;

import java.math.BigDecimal;

import com.opencsv.bean.CsvBindByName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LaptopSpecificationCsvRow {
    @CsvBindByName(column = "external_id")
    private String externalId;

    @CsvBindByName(column = "brand")
    private String brand;

    @CsvBindByName(column = "processor")
    private String processor;

    @CsvBindByName(column = "ram")
    private Integer ram;

    @CsvBindByName(column = "ssd")
    private Integer ssd;

    @CsvBindByName(column = "hard_disk")
    private Integer hardDisk;

    @CsvBindByName(column = "operating_system")
    private String operatingSystem;

    @CsvBindByName(column = "graphics")
    private String graphics;

    @CsvBindByName(column = "screen_size")
    private BigDecimal screenSize;

    @CsvBindByName(column = "resolution")
    private String resolution;
}