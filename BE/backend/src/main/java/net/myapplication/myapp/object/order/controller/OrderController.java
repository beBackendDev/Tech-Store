package net.myapplication.myapp.object.order.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.myapplication.myapp.object.order.dto.CreateOrderRequest;
import net.myapplication.myapp.object.order.dto.OrderResponseDto;
import net.myapplication.myapp.object.order.service.OrderService;
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
        public List<OrderResponseDto> getMyOrders(
                        @AuthenticationPrincipal UserDetailsImpl user) {

                return orderService.getMyOrders(
                                user.getId());
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
}
