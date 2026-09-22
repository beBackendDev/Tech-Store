package net.myapplication.myapp.object.admin.dto.dashboard;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;
import net.myapplication.myapp.enumpack.OrderStatus;
import net.myapplication.myapp.enumpack.PaymentStatus;

@Data 
@Builder 
public class RecentOrderDto {

    private Long orderId;

    private String customerName;

    private BigDecimal totalAmount;

    private OrderStatus status;

    private PaymentStatus paymentStatus;

    private LocalDateTime createdAt;

}
