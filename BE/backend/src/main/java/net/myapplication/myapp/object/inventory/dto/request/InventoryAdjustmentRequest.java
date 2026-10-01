package net.myapplication.myapp.object.inventory.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data 
public class InventoryAdjustmentRequest {

    @NotNull 
    @Min (0)
    private Integer actualStock;

    @NotBlank 
    @Size (max = 500)
    private String note;
}
