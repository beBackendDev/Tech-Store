package net.myapplication.myapp.object.order.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import net.myapplication.myapp.enumpack.OrderStatus;
import net.myapplication.myapp.object.order.dto.CreateOrderRequest;
import net.myapplication.myapp.object.order.dto.OrderResponseDto;
import net.myapplication.myapp.object.order.entity.Order;
import net.myapplication.myapp.object.product.dto.response.PageResponse;

public interface OrderService {

        PageResponse<OrderResponseDto> getMyOrders(Long userId, Pageable pageable);

        OrderResponseDto getOrderById(
                        Long orderId,
                        Long userId);

        OrderResponseDto createOrder(
                        CreateOrderRequest request,
                        Long userId);

        OrderResponseDto cancelOrder(
                        Long orderId,
                        Long userId,
                        String reason);

        OrderResponseDto requestReturn(
                        Long orderId,
                        Long userId);

        // ADMIN - APIs

        Page<OrderResponseDto> getAllOrders(
                        Pageable pageable);

        OrderResponseDto getOrderByIdForAdmin(
                        Long orderId);

        OrderResponseDto updateOrderStatus(
                        Long orderId,
                        OrderStatus newStatus);

        OrderResponseDto confirmOrder(
                        Long orderId);

        OrderResponseDto processOrder(
                        Long orderId);

        OrderResponseDto shipOrder(
                        Long orderId);

        OrderResponseDto deliverOrder(
                        Long orderId);

        OrderResponseDto completeOrder(
                        Long orderId);

        OrderResponseDto cancelOrderByAdmin(
                        Long orderId,
                        String reason);

        // =====================================================
        // ADMIN - RETURN
        // =====================================================

        OrderResponseDto completeReturn(
                        Long orderId);

        // =====================================================
        // PAYMENT - FLOW
        // =====================================================

        OrderResponseDto markPaymentSuccess(
                        Long orderId);

        OrderResponseDto markPaymentFailed(
                        Long orderId,
                        String reason);

        OrderResponseDto completeCodOrder(
                        Long orderId);
}
