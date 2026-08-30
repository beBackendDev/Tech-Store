package net.myapplication.myapp.object.order.service;

import java.util.List;

import net.myapplication.myapp.object.order.dto.CreateOrderRequest;
import net.myapplication.myapp.object.order.dto.OrderResponseDto;

public interface OrderService {
    OrderResponseDto createOrder(
            CreateOrderRequest request,
            Long userId);

    List<OrderResponseDto> getMyOrders(Long userId);

    OrderResponseDto getOrderById(
            Long orderId,
            Long userId);
}
