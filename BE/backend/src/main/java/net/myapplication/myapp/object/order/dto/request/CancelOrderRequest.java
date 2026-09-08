package net.myapplication.myapp.object.order.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data 
public class CancelOrderRequest {

    @NotBlank 
    private String reason;
}
