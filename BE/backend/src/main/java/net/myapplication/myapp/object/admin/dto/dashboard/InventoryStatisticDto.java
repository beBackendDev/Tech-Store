package net.myapplication.myapp.object.admin.dto.dashboard;

import lombok.Builder;
import lombok.Data;

@Data 
@Builder 
public class InventoryStatisticDto {

    private Long stockInToday;

    private Long stockOutToday;

    private Long reservedToday;

    private Long releasedToday;

    private Long adjustmentToday;

}
