package net.myapplication.myapp.object.order.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.myapplication.myapp.common.ApiResponseDTO;
import net.myapplication.myapp.object.order.dto.CreateOrderRequest;
import net.myapplication.myapp.object.order.dto.OrderResponseDto;
import net.myapplication.myapp.object.order.dto.request.CancelOrderRequest;
import net.myapplication.myapp.object.order.service.OrderService;
import net.myapplication.myapp.object.product.dto.PageResponse;
import net.myapplication.myapp.security.oauth2.service.CurrentUserService;
import net.myapplication.myapp.user.service.impl.UserDetailsImpl;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
        private final OrderService orderService;
        private final CurrentUserService currentUserService;

        // get orders
@GetMapping
public ResponseEntity<PageResponse<OrderResponseDto>> getMyOrders(
        @AuthenticationPrincipal UserDetailsImpl user,

        @PageableDefault(
                size = 10,
                sort = "createdAt",
                direction = Sort.Direction.DESC
        )
        Pageable pageable) {

    PageResponse<OrderResponseDto> response =
            orderService.getMyOrders(
                    user.getId(),
                    pageable
            );

    return ResponseEntity.ok(response);
}

        // get orders by id
        @GetMapping("/{id}")
        public OrderResponseDto getOrderById(@PathVariable Long id,
                        @AuthenticationPrincipal UserDetailsImpl user) {
                return orderService.getOrderById(
                                id,
                                user.getId());
        }

        @PostMapping
        @PreAuthorize("isAuthenticated()")
        public ResponseEntity<OrderResponseDto> createOrder(
                        @Valid @RequestBody CreateOrderRequest request,
                        Principal principal) {

                Long userId = currentUserService.getCurrentUserId();

                OrderResponseDto response = orderService.createOrder(
                                request,
                                userId);

                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(response);
        }

        @PatchMapping("/{id}/cancel")
        @PreAuthorize("isAuthenticated()")
        public ResponseEntity<ApiResponseDTO<OrderResponseDto>> cancelOrder(

                        @PathVariable Long id,

                        @Valid @RequestBody CancelOrderRequest request,

                        @AuthenticationPrincipal UserDetailsImpl user) {

                OrderResponseDto response = orderService.cancelOrder(

                                id,

                                user.getId(),

                                request.getReason());

                return ResponseEntity.ok(

                                ApiResponseDTO
                                                .<OrderResponseDto>builder()

                                                .status("SUCCESS")

                                                .message(
                                                                "Order cancelled successfully")

                                                .response(response)

                                                .build());
        }
}
