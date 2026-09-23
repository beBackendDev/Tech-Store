package net.myapplication.myapp.object.order.mapper;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import net.myapplication.myapp.object.admin.dto.dashboard.RecentOrderDto;
import net.myapplication.myapp.object.order.entity.Order;

@Component
@RequiredArgsConstructor
public class RecentOrderMapper {
        public RecentOrderDto mapToRecentOrderDto(
                        Order order) {

                return RecentOrderDto.builder()

                                .orderId(
                                                order.getId())

                                .customerName(
                                                order.getCustomerName())

                                .totalAmount(
                                                order.getTotalAmount())

                                .status(
                                                order.getStatus())

                                .paymentStatus(
                                                order.getPaymentStatus())

                                .createdAt(
                                                order.getCreatedAt())

                                .build();
        }
}
