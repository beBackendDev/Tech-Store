package net.myapplication.myapp.object.order.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import net.myapplication.myapp.enumpack.OrderStatus;

@Data 
public class UpdateOrderStatusRequest {

    @NotNull 
    private OrderStatus status;
}
