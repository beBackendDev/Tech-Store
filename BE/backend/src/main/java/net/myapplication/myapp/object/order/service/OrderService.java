package net.myapplication.myapp.object.order.service;

import java.util.List;

import net.myapplication.myapp.enumpack.OrderStatus;
import net.myapplication.myapp.object.order.dto.CreateOrderRequest;
import net.myapplication.myapp.object.order.dto.OrderResponseDto;

public interface OrderService {
        OrderResponseDto createOrder(
                        CreateOrderRequest request,
                        Long userId);

        List<OrderResponseDto> getMyOrders(Long userId);

        public void markPaymentSuccess(
                        Long orderId);

        public void markPaymentFailed(
                        Long orderId,
                        String reason);

        public void completeCodOrder(
                        Long orderId);

        public OrderResponseDto updateOrderStatus(
                        Long orderId,
                        OrderStatus newStatus);

        OrderResponseDto getOrderById(
                        Long orderId,
                        Long userId);

        OrderResponseDto cancelOrder(
                        Long orderId,
                        Long userId,
                        String reason);
}
