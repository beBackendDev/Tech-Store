package net.myapplication.myapp.user.dto.request;

import lombok.Data;
import net.myapplication.myapp.enumpack.OrderStatus;
import net.myapplication.myapp.enumpack.PaymentStatus;

@Data
public class AdminOrderFilterRequest {

    private String keyword;

    private OrderStatus status;

    private PaymentStatus paymentStatus;
}
