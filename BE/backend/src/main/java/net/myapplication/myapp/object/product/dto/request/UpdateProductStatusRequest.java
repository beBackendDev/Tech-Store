package net.myapplication.myapp.object.product.dto.request;
import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class UpdateProductStatusRequest {

    @NotNull
    private Boolean active;
}