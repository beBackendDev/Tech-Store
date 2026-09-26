package net.myapplication.myapp.object.admin.dto.admin.product;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data 
public class UpdateProductStatusRequest {

    @NotNull 
    private Boolean active;
}
