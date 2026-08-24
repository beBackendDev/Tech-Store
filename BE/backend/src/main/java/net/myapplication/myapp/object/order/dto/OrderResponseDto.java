package net.myapplication.myapp.object.order.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Data;

import net.myapplication.myapp.enumpack.OrderStatus;
import net.myapplication.myapp.enumpack.PaymentMethod;
import net.myapplication.myapp.enumpack.PaymentStatus;

@Data
@Builder
public class OrderResponseDto {

    private Long id;

    private String customerName;

    private String email;

    private String phone;

    private String address;

    private String city;

    private String district;

    private String note;

    private BigDecimal subtotal;

    private BigDecimal shippingFee;

    private BigDecimal discount;

    private BigDecimal totalAmount;

    private OrderStatus status;

    private PaymentMethod paymentMethod;

    private PaymentStatus paymentStatus;

    private LocalDateTime createdAt;

    private List<OrderItemResponseDto> items;
}