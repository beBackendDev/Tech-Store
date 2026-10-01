package net.myapplication.myapp.object.inventory.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data 
public class StockOutRequest {

    @NotNull 
    @Min (1)
    private Integer quantity;

    private String note;
}
