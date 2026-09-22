package net.myapplication.myapp.object.admin.dto.dashboard;

import lombok.Builder;
import lombok.Data;

@Data 
@Builder 
public class ProductStatisticDto {

    private Long totalProducts;

    private Long totalStock;

    private Long activeProducts;

    private Long inactiveProducts;

    private Long outOfStockProducts;

    private Long lowStockProducts;

}
