package net.myapplication.myapp.user.service;

import org.springframework.data.domain.Pageable;

import net.myapplication.myapp.enumpack.OrderStatus;
import net.myapplication.myapp.object.order.dto.OrderResponseDto;
import net.myapplication.myapp.object.product.dto.PageResponse;
import net.myapplication.myapp.user.dto.request.AdminOrderFilterRequest;

public interface AdminOrderService {

    PageResponse<OrderResponseDto> getOrders(
            AdminOrderFilterRequest filter,
            Pageable pageable
    );

    OrderResponseDto getOrderById(
            Long orderId
    );

    OrderResponseDto updateOrderStatus(
            Long orderId,
            OrderStatus status
    );
}