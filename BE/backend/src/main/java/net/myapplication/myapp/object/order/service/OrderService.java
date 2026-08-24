package net.myapplication.myapp.object.order.service;

import net.myapplication.myapp.object.order.dto.CreateOrderRequest;
import net.myapplication.myapp.object.order.dto.OrderResponseDto;

public interface OrderService {
    OrderResponseDto createOrder(
            CreateOrderRequest request,
            Long userId
    );
}
