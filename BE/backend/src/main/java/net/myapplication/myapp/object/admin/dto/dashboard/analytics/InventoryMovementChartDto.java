package net.myapplication.myapp.object.admin.dto.dashboard.analytics;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.myapplication.myapp.object.inventory.enums.InventoryTransactionType;

@Data 
@Builder
@NoArgsConstructor 
@AllArgsConstructor 
public class InventoryMovementChartDto {

    private LocalDate date;

    private InventoryTransactionType type;

    private Long quantity;
}
