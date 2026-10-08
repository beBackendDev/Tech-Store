package net.myapplication.myapp.object.order.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PaymentFailedRequest {

    @NotBlank
    @Size(max = 500)
    private String reason;
}