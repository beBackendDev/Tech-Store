package net.myapplication.myapp.user.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import net.myapplication.myapp.enumpack.OrderStatus;
import net.myapplication.myapp.object.order.dto.OrderResponseDto;
import net.myapplication.myapp.object.order.entity.Order;
import net.myapplication.myapp.object.order.mapper.OrderMapper;
import net.myapplication.myapp.object.order.repository.OrderRepository;
import net.myapplication.myapp.object.order.specification.OrderSpecification;
import net.myapplication.myapp.object.product.dto.PageResponse;
import net.myapplication.myapp.user.dto.request.AdminOrderFilterRequest;
import net.myapplication.myapp.user.service.AdminOrderService;

@Service 
@RequiredArgsConstructor 
@Transactional (readOnly = true)
public class AdminOrderServiceImpl
        implements AdminOrderService {

    private final OrderRepository orderRepository;

    private final OrderMapper orderMapper;

    @Override
    public PageResponse<OrderResponseDto> getOrders(
            AdminOrderFilterRequest filter,
            Pageable pageable
    ) {

        Specification<Order> specification =
                OrderSpecification.filter(filter);

        Page<Order> orderPage =
                orderRepository.findAll(
                        specification,
                        pageable
                );

        Page<OrderResponseDto> responsePage =
                orderPage.map(
                        orderMapper::toResponseDto
                );

        return PageResponse
                .<OrderResponseDto>builder()

                .content(
                        responsePage.getContent()
                )

                .page(
                        responsePage.getNumber()
                )

                .size(
                        responsePage.getSize()
                )

                .totalElements(
                        responsePage.getTotalElements()
                )

                .totalPages(
                        responsePage.getTotalPages()
                )

                .first(
                        responsePage.isFirst()
                )

                .last(
                        responsePage.isLast()
                )

                .build();
    }

    @Override
    public OrderResponseDto getOrderById(Long orderId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public OrderResponseDto updateOrderStatus(Long orderId, OrderStatus status) {
        // TODO Auto-generated method stub
        return null;
    }
}
