package net.myapplication.myapp.config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "data.pipeline")
@Getter
@Setter
public class DataPipelineProperties {

    private String imageDir;

    private String productsCsv;

    private String laptopSpecificationsCsv;
}