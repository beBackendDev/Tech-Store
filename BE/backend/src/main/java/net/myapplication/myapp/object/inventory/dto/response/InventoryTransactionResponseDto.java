package net.myapplication.myapp.object.inventory.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.myapplication.myapp.object.inventory.enums.InventoryTransactionType;

@Data 
@Builder 
@NoArgsConstructor 
@AllArgsConstructor 
public class InventoryTransactionResponseDto {

    private Long id;

    private Long productId;

    private String productName;

    private Long orderId;

    private InventoryTransactionType type;

    private Integer quantity;

    private Integer stockBefore;

    private Integer stockAfter;

    private Integer reservedBefore;

    private Integer reservedAfter;

    private String note;

    private LocalDateTime createdAt;
}
