package net.myapplication.myapp.object.order.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import net.myapplication.myapp.enumpack.OrderStatus;
import net.myapplication.myapp.object.order.dto.CreateOrderRequest;
import net.myapplication.myapp.object.order.dto.OrderResponseDto;
import net.myapplication.myapp.object.product.dto.PageResponse;

public interface OrderService {
        OrderResponseDto createOrder(
                        CreateOrderRequest request,
                        Long userId);

        PageResponse<OrderResponseDto> getMyOrders(Long userId, Pageable pageable);

        void markPaymentSuccess(
                        Long orderId);

        void markPaymentFailed(
                        Long orderId,
                        String reason);

        void completeCodOrder(
                        Long orderId);

        OrderResponseDto updateOrderStatus(
                        Long orderId,
                        OrderStatus newStatus);

        OrderResponseDto getOrderById(
                        Long orderId,
                        Long userId);

        OrderResponseDto requestReturn(
                        Long orderId,
                        Long userId);

        OrderResponseDto confirmReturn(
                        Long orderId);

        OrderResponseDto cancelOrder(
                        Long orderId,
                        Long userId,
                        String reason);
}
