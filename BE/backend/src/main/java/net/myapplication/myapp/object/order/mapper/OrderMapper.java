package net.myapplication.myapp.object.order.mapper;

import java.util.List;


import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import net.myapplication.myapp.object.order.dto.OrderItemResponseDto;
import net.myapplication.myapp.object.order.dto.OrderResponseDto;
import net.myapplication.myapp.object.order.entity.Order;
import net.myapplication.myapp.object.order.entity.OrderItem;

@Component
@RequiredArgsConstructor
public class OrderMapper {

    public OrderResponseDto toResponseDto(Order order) {

        List<OrderItemResponseDto> items =
                order.getItems()
                        .stream()
                        .map(this::toItemResponseDto)
                        .toList();

        return OrderResponseDto.builder()

                .id(order.getId())

                .customerName(order.getCustomerName())

                .email(order.getEmail())

                .phone(order.getPhone())

                .address(order.getAddress())

                .city(order.getCity())

                .district(order.getDistrict())

                .note(order.getNote())

                .subtotal(order.getSubtotal())

                .shippingFee(order.getShippingFee())

                .discount(order.getDiscount())

                .totalAmount(order.getTotalAmount())

                .status(order.getStatus())

                .paymentMethod(order.getPaymentMethod())

                .paymentStatus(order.getPaymentStatus())

                .createdAt(order.getCreatedAt())

                .items(items)

                .build();
    }


    private OrderItemResponseDto toItemResponseDto(
            OrderItem item
    ) {

        return OrderItemResponseDto.builder()

                .id(item.getId())

                .productId(
                        item.getProduct().getId()
                )

                .productName(
                        item.getProductName()
                )

                .productImage(
                        item.getProductImage()
                )

                .price(
                        item.getPrice()
                )

                .quantity(
                        item.getQuantity()
                )

                .subtotal(
                        item.getSubtotal()
                )

                .build();
    }
}
