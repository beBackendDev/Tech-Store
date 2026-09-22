package net.myapplication.myapp.object.admin.dto.dashboard;

import lombok.Builder;
import lombok.Data;

@Data 
@Builder 
public class LowStockProductDto {

    private Long productId;

    private String productName;

    private Integer stock;

    private String image;

}
