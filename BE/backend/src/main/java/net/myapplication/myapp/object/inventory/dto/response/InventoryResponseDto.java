package net.myapplication.myapp.object.inventory.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.myapplication.myapp.object.inventory.enums.InventoryStockStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryResponseDto {

    private Long productId;

    private String externalId;

    private String productName;

    private String category;

    private String image;

    private Integer stock;

    private Integer reservedStock;

    private Integer availableStock;

    private InventoryStockStatus status;

}
