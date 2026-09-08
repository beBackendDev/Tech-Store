package net.myapplication.myapp.user.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
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
import net.myapplication.myapp.object.order.dto.OrderResponseDto;
import net.myapplication.myapp.object.order.dto.request.UpdateOrderStatusRequest;
import net.myapplication.myapp.object.order.service.OrderService;
import net.myapplication.myapp.object.product.dto.PageResponse;
import net.myapplication.myapp.object.product.service.ProductImportService;

@RestController
@RequestMapping("/api/dashboard/admin")
@RequiredArgsConstructor
public class AdminController {
    private final ProductImportService productImportService;

    private final OrderService orderService;

    @PostMapping("/import/products")
    public ResponseEntity<String> importProducts() {

        productImportService.importAll();

        return ResponseEntity.ok(
                "Product import completed successfully.");
    }

    // @GetMapping("/orders")
    // public ResponseEntity<ApiResponseDTO<PageResponse<OrderResponseDto>>> getOrders(
    //         Pageable pageable) {

    //     PageResponse<OrderResponseDto> response = orderService.getOrders(
    //             pageable);

    //     return ResponseEntity.ok(

    //             ApiResponseDTO
    //                     .<PageResponse<OrderResponseDto>>builder()

    //                     .status("SUCCESS")

    //                     .message(
    //                             "Orders retrieved successfully")

    //                     .response(response)

    //                     .build());
    // }

    @PatchMapping("/orders/{id}/status")
    public ResponseEntity<ApiResponseDTO<OrderResponseDto>> updateOrderStatus(

            @PathVariable Long id,

            @Valid @RequestBody UpdateOrderStatusRequest request) {

        OrderResponseDto response = orderService.updateOrderStatus(

                id,

                request.getStatus());

        return ResponseEntity.ok(

                ApiResponseDTO
                        .<OrderResponseDto>builder()

                        .status("SUCCESS")

                        .message(
                                "Order status updated successfully")

                        .response(response)

                        .build());
    }
}
