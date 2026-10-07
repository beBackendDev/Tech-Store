package net.myapplication.myapp.object.order.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import net.myapplication.myapp.enumpack.OrderStatus;
import net.myapplication.myapp.enumpack.PaymentMethod;
import net.myapplication.myapp.enumpack.PaymentStatus;
import net.myapplication.myapp.object.inventory.enums.InventoryTransactionType;
import net.myapplication.myapp.object.inventory.service.InventoryService;
import net.myapplication.myapp.object.order.dto.CreateOrderRequest;
import net.myapplication.myapp.object.order.dto.OrderItemRequest;
import net.myapplication.myapp.object.order.dto.OrderResponseDto;
import net.myapplication.myapp.object.order.entity.Order;
import net.myapplication.myapp.object.order.entity.OrderItem;
import net.myapplication.myapp.object.order.mapper.OrderMapper;
import net.myapplication.myapp.object.order.repository.OrderItemRepository;
import net.myapplication.myapp.object.order.repository.OrderRepository;
import net.myapplication.myapp.object.order.service.OrderService;
import net.myapplication.myapp.object.product.dto.response.PageResponse;
import net.myapplication.myapp.object.product.entity.Product;
import net.myapplication.myapp.object.product.repository.ProductRepository;
import net.myapplication.myapp.user.entity.User;
import net.myapplication.myapp.user.repository.UserRepo;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
        private final OrderRepository orderRepository;

        private final ProductRepository productRepository;

        private final UserRepo userRepository;

        private final InventoryService inventoryService;

        private final OrderMapper orderMapper;

        @Override
        @Transactional
        public OrderResponseDto createOrder(CreateOrderRequest request, Long userId) {
                // Find user
                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new RuntimeException("User not found"));
                // merge duplicate product
                Map<Long, Integer> requestedProducts = request.getItems()
                                .stream()
                                .collect(
                                                Collectors.toMap(

                                                                OrderItemRequest::getProductId,

                                                                OrderItemRequest::getQuantity,

                                                                Integer::sum));
                // Create Order
                Order order = new Order();
                order.setUser(user);

                // Create Customer snapshot
                String customerName = request.getFirstName() + " " + request.getLastName();
                order.setCustomerName(customerName);
                order.setEmail(request.getEmail());
                order.setPhone(request.getPhone());

                // Create Shipping snapshot
                order.setAddress(request.getAddress());
                order.setCity(
                                request.getCity());

                order.setDistrict(
                                request.getDistrict());

                order.setNote(
                                request.getNote());

                // Create Payment
                order.setPaymentMethod(
                                request.getPaymentMethod());

                order.setPaymentStatus(
                                PaymentStatus.PENDING);

                // Order status
                initializeOrderStatus(order);

                // Calculate subtotal
                BigDecimal subtotal = BigDecimal.ZERO;

                for (Map.Entry<Long, Integer> entry : requestedProducts.entrySet()) {

                        Long productId = entry.getKey();

                        Integer quantity = entry.getValue();
                        // validate product existence and availability
                        Product product = productRepository
                                        .findByIdForUpdate(
                                                        productId)
                                        .orElseThrow(() -> new RuntimeException(
                                                        "Product not found: "
                                                                        + productId));

                        // VALIDATE ORDERS
                        validateProduct(
                                        product,
                                        quantity);

                        // current price
                        BigDecimal price = product.getPrice();
                        // item subtotal
                        BigDecimal itemSubtotal = price.multiply(
                                        BigDecimal.valueOf(
                                                        quantity));

                        subtotal = subtotal.add(
                                        itemSubtotal);
                        // create order item
                        OrderItem orderItem = new OrderItem();

                        orderItem.setProduct(
                                        product);

                        orderItem.setProductName(
                                        product.getName());

                        orderItem.setProductImage(
                                        product.getImage());

                        orderItem.setPrice(
                                        price);

                        orderItem.setQuantity(
                                        quantity);

                        orderItem.setSubtotal(
                                        itemSubtotal);
                        // add to order
                        order.addItem(
                                        orderItem);

                }
                // shipping fee
                BigDecimal shippingFee = calculateShippingFee(
                                subtotal);
                // discount
                BigDecimal discount = BigDecimal.ZERO;
                // total
                BigDecimal totalAmount = subtotal
                                .add(shippingFee)
                                .subtract(discount);

                order.setSubtotal(
                                subtotal);

                order.setShippingFee(
                                shippingFee);

                order.setDiscount(
                                discount);

                order.setTotalAmount(
                                totalAmount);
                Order savedOrder = orderRepository.save(
                                order);

                // RESERVE INVENTORY
                reserveOrderInventory(
                                savedOrder);
                return orderMapper.toResponseDto(
                                savedOrder);
        }

        @Override
        @Transactional(readOnly = true)
        public PageResponse<OrderResponseDto> getMyOrders(
                        Long userId,
                        Pageable pageable) {

                Page<Order> page = orderRepository
                                .findByUserId(
                                                userId,
                                                pageable);

                return PageResponse
                                .<OrderResponseDto>builder()

                                .content(
                                                page.getContent()
                                                                .stream()
                                                                .map(
                                                                                orderMapper::toResponseDto)
                                                                .toList())

                                .page(
                                                page.getNumber())

                                .size(
                                                page.getSize())

                                .totalElements(
                                                page.getTotalElements())

                                .totalPages(
                                                page.getTotalPages())

                                .first(
                                                page.isFirst())

                                .last(
                                                page.isLast())

                                .build();
        }

        @Override
        @Transactional(readOnly = true)
        public OrderResponseDto getOrderById(
                        Long orderId,
                        Long userId) {

                Order order = orderRepository
                                .findByIdAndUserId(orderId, userId)
                                .orElseThrow(() -> new RuntimeException(
                                                "Order not found"));

                return orderMapper.toResponseDto(order);
        }

        @Override
        @Transactional
        public OrderResponseDto cancelOrder(
                        Long orderId,
                        Long userId,
                        String reason) {

                Order order = orderRepository
                                .findByIdAndUserId(
                                                orderId,
                                                userId)
                                .orElseThrow(() -> new RuntimeException(
                                                "Order not found"));

                // =========================================================
                // VALIDATE STATUS
                // =========================================================

                validateOrderCanBeCancelled(
                                order);

                // =========================================================
                // RELEASE INVENTORY
                // =========================================================

                releaseOrderInventory(
                                order,
                                reason);

                // =========================================================
                // CANCEL ORDER
                // =========================================================

                order.setStatus(
                                OrderStatus.CANCELLED);

                // =========================================================
                // PAYMENT STATUS
                // =========================================================

                if (order.getPaymentStatus() == PaymentStatus.PENDING) {

                        order.setPaymentStatus(
                                        PaymentStatus.FAILED);
                }

                return orderMapper.toResponseDto(
                                order);
        }

        @Override
        @Transactional
        public OrderResponseDto requestReturn(
                        Long orderId,
                        Long userId) {

                Order order = orderRepository.findByIdAndUserId(
                                orderId,
                                userId).orElseThrow(
                                                () -> new RuntimeException(
                                                                "Order not found: " + orderId));

                if (order.getStatus() != OrderStatus.COMPLETED) {

                        throw new IllegalStateException(
                                        "Only completed orders can request a return.");
                }

                order.setStatus(
                                OrderStatus.RETURN_REQUESTED);

                return orderMapper.toResponseDto(
                                orderRepository.save(order));
        }

        // ADMIN APIs
        @Override
        @Transactional
        public void markPaymentSuccess(
                        Long orderId) {

                Order order = orderRepository
                                .findById(orderId)
                                .orElseThrow(() -> new RuntimeException(
                                                "Order not found"));

                // =========================================================
                // IDEMPOTENCY
                // =========================================================

                if (order.getPaymentStatus() == PaymentStatus.PAID) {

                        return;
                }

                // =========================================================
                // VALIDATE
                // =========================================================

                if (order.getStatus() == OrderStatus.CANCELLED) {

                        throw new IllegalStateException(
                                        "Cannot pay cancelled order");
                }

                // =========================================================
                // PAYMENT SUCCESS
                // =========================================================

                order.setPaymentStatus(
                                PaymentStatus.PAID);

                // =========================================================
                // COMMIT INVENTORY
                // =========================================================

                commitOrderInventory(
                                order);

                // =========================================================
                // UPDATE ORDER
                // =========================================================

                order.setStatus(
                                OrderStatus.CONFIRMED);
        }

        @Override
        @Transactional
        public void markPaymentFailed(
                        Long orderId,
                        String reason) {

                Order order = orderRepository
                                .findById(orderId)
                                .orElseThrow(() -> new RuntimeException(
                                                "Order not found"));

                if (order.getPaymentStatus() == PaymentStatus.PAID) {

                        throw new IllegalStateException(
                                        "Order already paid");
                }

                order.setPaymentStatus(
                                PaymentStatus.FAILED);

                releaseOrderInventory(
                                order,
                                "Payment failed: "
                                                + reason);

                order.setStatus(
                                OrderStatus.CANCELLED);
        }

        @Override
        @Transactional
        public void completeCodOrder(
                        Long orderId) {

                Order order = orderRepository
                                .findById(orderId)
                                .orElseThrow(() -> new RuntimeException(
                                                "Order not found"));

                if (order.getPaymentMethod() != PaymentMethod.COD) {

                        throw new IllegalStateException(
                                        "Order is not COD");
                }

                if (order.getStatus() != OrderStatus.DELIVERED) {

                        throw new IllegalStateException(
                                        "Order must be delivered first");
                }

                // Payment

                order.setPaymentStatus(
                                PaymentStatus.PAID);

                // Commit inventory

                commitOrderInventory(
                                order);

                // Complete

                order.setStatus(
                                OrderStatus.DELIVERED);
        }

        @Override
        @Transactional
        public OrderResponseDto updateOrderStatus(
                        Long orderId,
                        OrderStatus newStatus) {

                Order order = orderRepository
                                .findById(orderId)
                                .orElseThrow(() -> new RuntimeException(
                                                "Order not found"));

                validateStatusTransition(
                                order,
                                newStatus);

                // =========================================================
                // CONFIRMED → PROCESSING
                // =========================================================

                if (order.getStatus() == OrderStatus.CONFIRMED

                                &&

                                newStatus == OrderStatus.PROCESSING) {

                        commitOrderInventory(
                                        order);
                }

                order.setStatus(
                                newStatus);

                return orderMapper.toResponseDto(
                                order);
        }

        // @Override
        // @Transactional
        // public OrderResponseDto requestReturn(
        // Long orderId,
        // Long userId) {

        // Order order = orderRepository
        // .findByIdAndUserId(
        // orderId,
        // userId)
        // .orElseThrow(() -> new RuntimeException(
        // "Order not found"));

        // if (order.getStatus() != OrderStatus.DELIVERED

        // &&

        // order.getStatus() != OrderStatus.COMPLETED) {

        // throw new IllegalStateException(
        // "Order cannot be returned");
        // }

        // order.setStatus(
        // OrderStatus.RETURN_REQUESTED);

        // return orderMapper.toResponseDto(
        // order);
        // }

        // @Override
        // @Transactional
        // public OrderResponseDto confirmReturn(
        // Long orderId) {

        // Order order = orderRepository
        // .findById(orderId)
        // .orElseThrow(() -> new RuntimeException(
        // "Order not found"));

        // if (order.getStatus() != OrderStatus.RETURN_REQUESTED) {

        // throw new IllegalStateException(
        // "Return was not requested");
        // }

        // // =========================================================
        // // RESTOCK
        // // =========================================================

        // for (OrderItem item : order.getItems()) {

        // inventoryService.returnStock(

        // item.getProduct().getId(),

        // item.getQuantity(),

        // order,

        // "Product returned by customer");
        // }

        // // =========================================================
        // // UPDATE ORDER
        // // =========================================================

        // order.setStatus(
        // OrderStatus.RETURNED);

        // // =========================================================
        // // PAYMENT REFUND
        // // =========================================================

        // if (order.getPaymentStatus() == PaymentStatus.PAID) {

        // order.setPaymentStatus(
        // PaymentStatus.REFUNDED);
        // }

        // return orderMapper.toResponseDto(
        // order);
        // }

        @Transactional
        @Override
        public OrderResponseDto shipOrder(
                        Long orderId) {
                Order order = getOrder(
                                orderId);

                validateCanShip(order);

                for (OrderItem item : order.getItems()) {

                        inventoryService.commitReservedStock(
                                        item.getProduct().getId(),
                                        item.getQuantity(),
                                        order);
                }

                order.setStatus(OrderStatus.SHIPPING);

                orderRepository.save(order);
                return orderMapper.toResponseDto(order);
        }

        // helper

        private void validateOrderCanBeCancelled(
                        Order order) {

                OrderStatus status = order.getStatus();

                if (status == OrderStatus.CANCELLED) {

                        throw new IllegalStateException(
                                        "Order already cancelled");
                }

                if (status == OrderStatus.PROCESSING ||
                                status == OrderStatus.DELIVERED ||
                                status == OrderStatus.COMPLETED ||
                                status == OrderStatus.RETURNED) {

                        throw new IllegalStateException(
                                        "Order cannot be cancelled");
                }
        }

        private void validateCanShip(Order order) {
                if (order.getStatus() != OrderStatus.PROCESSING) {
                        throw new IllegalStateException(
                                        "Only processing orders can be shipped.");
                }
        }

        private void validateCanReturn(Order order) {
                if (order.getStatus() != OrderStatus.RETURN_REQUESTED) {
                        throw new IllegalStateException(
                                        "Order must be in RETURN_REQUESTED status.");
                }
        }

        private void releaseOrderInventory(
                        Order order,
                        String reason) {

                for (OrderItem item : order.getItems()) {

                        inventoryService
                                        .releaseReservedStock(

                                                        item.getProduct().getId(),

                                                        item.getQuantity(),

                                                        order,

                                                        "Order cancelled: "
                                                                        + reason);
                }
        }

        private void validateStatusTransition(
                        Order order,
                        OrderStatus newStatus) {

                OrderStatus current = order.getStatus();

                switch (current) {

                        case PENDING -> {

                                if (newStatus != OrderStatus.CONFIRMED

                                                &&

                                                newStatus != OrderStatus.CANCELLED) {

                                        throw new IllegalStateException(
                                                        "Invalid order status transition");
                                }
                        }

                        case CONFIRMED -> {

                                if (newStatus != OrderStatus.PROCESSING

                                                &&

                                                newStatus != OrderStatus.CANCELLED) {

                                        throw new IllegalStateException(
                                                        "Invalid order status transition");
                                }
                        }

                        case PROCESSING -> {

                                if (newStatus != OrderStatus.SHIPPING) {

                                        throw new IllegalStateException(
                                                        "Invalid order status transition");
                                }
                        }

                        case SHIPPING -> {

                                if (newStatus != OrderStatus.DELIVERED) {

                                        throw new IllegalStateException(
                                                        "Invalid order status transition");
                                }
                        }

                        case DELIVERED -> {

                                if (newStatus != OrderStatus.COMPLETED

                                                &&

                                                newStatus != OrderStatus.RETURN_REQUESTED) {

                                        throw new IllegalStateException(
                                                        "Invalid order status transition");
                                }
                        }

                        case RETURN_REQUESTED -> {

                                if (newStatus != OrderStatus.RETURNED) {

                                        throw new IllegalStateException(
                                                        "Invalid order status transition");
                                }
                        }

                        default ->

                                throw new IllegalStateException(
                                                "Order cannot change status");
                }
        }

        private void initializeOrderStatus(
                        Order order) {

                if (order.getPaymentMethod() == PaymentMethod.COD) {

                        order.setStatus(
                                        OrderStatus.PENDING);

                        order.setPaymentStatus(
                                        PaymentStatus.PENDING);

                        return;
                }

                order.setStatus(
                                OrderStatus.PENDING);

                order.setPaymentStatus(
                                PaymentStatus.PENDING);
        }

        private void validateProduct(
                        Product product,
                        Integer quantity) {

                if (!product.isActive()) {

                        throw new IllegalStateException(
                                        "Product is no longer available: "
                                                        + product.getName());
                }

                if (quantity == null ||
                                quantity <= 0) {

                        throw new IllegalArgumentException(
                                        "Invalid quantity");
                }

                if (product.getAvailableStock() < quantity) {

                        throw new IllegalStateException(
                                        "Insufficient stock for product: "
                                                        + product.getName());
                }
        }

        private void reserveOrderInventory(
                        Order order) {

                for (OrderItem item : order.getItems()) {

                        inventoryService.reserveStock(
                                        item.getProduct().getId(),
                                        item.getQuantity(),
                                        order);
                }
        }

        private BigDecimal calculateShippingFee(
                        BigDecimal subtotal) {

                BigDecimal freeShippingThreshold = BigDecimal.valueOf(
                                500_000);

                if (subtotal.compareTo(
                                freeShippingThreshold) >= 0) {

                        return BigDecimal.ZERO;
                }

                return BigDecimal.valueOf(
                                30_000);
        }

        private void commitOrderInventory(
                        Order order) {

                for (OrderItem item : order.getItems()) {

                        inventoryService
                                        .commitReservedStock(

                                                        item.getProduct().getId(),

                                                        item.getQuantity(),

                                                        order);
                }
        }

        @Override
        @Transactional
        public OrderResponseDto cancelOrderByAdmin(
                        Long orderId,
                        String reason) {

                Order order = getOrder(
                                orderId);
                String note = "CANCELLED BY ADMIN: " + reason;
                validateCanCancel(order);

                /*
                 * Only orders that still have reserved inventory
                 * should release inventory.
                 */
                for (OrderItem item : order.getItems()) {

                        inventoryService.releaseStock(
                                        item.getProduct().getId(),
                                        item.getQuantity(),
                                        note);
                }

                order.setStatus(
                                OrderStatus.CANCELLED);

                // order.setCancellationReason(reason);

                return orderMapper.toResponseDto(
                                orderRepository.save(order));
        }

        @Override
        @Transactional
        public OrderResponseDto completeOrder(
                        Long orderId) {
                Order order = getOrder(
                                orderId);

                if (order.getStatus() != OrderStatus.DELIVERED) {

                        throw new IllegalStateException(
                                        "Only DELIVERED orders can be completed.");
                }

                order.setStatus(
                                OrderStatus.COMPLETED);

                return orderMapper.toResponseDto(
                                orderRepository.save(order));
        }

        @Override
        @Transactional
        public OrderResponseDto completeReturn(
                        Long orderId) {

                Order order = getOrder(
                                orderId);

                if (order.getStatus() != OrderStatus.RETURN_REQUESTED) {

                        throw new IllegalStateException(
                                        "Only RETURN_REQUESTED orders can be completed.");
                }

                /*
                 * Returned products re-enter inventory.
                 */
                for (OrderItem item : order.getItems()) {

                        inventoryService.returnStock(
                                        item.getProduct().getId(),
                                        item.getQuantity(),
                                        order,
                                        "Returned from order #" + order.getId());
                }

                order.setStatus(
                                OrderStatus.RETURNED);

                return orderMapper.toResponseDto(
                                orderRepository.save(order));
        }

        @Override
        @Transactional
        public OrderResponseDto confirmOrder(
                        Long orderId) {

                Order order = getOrder(
                                orderId);

                if (order.getStatus() != OrderStatus.PENDING) {

                        throw new IllegalStateException(
                                        "Only PENDING orders can be confirmed.");
                }

                order.setStatus(
                                OrderStatus.CONFIRMED);

                return orderMapper.toResponseDto(
                                orderRepository.save(order));
        }

        @Override
        @Transactional
        public OrderResponseDto deliverOrder(
                        Long orderId) {

                Order order = getOrder(
                                orderId);

                if (order.getStatus() != OrderStatus.SHIPPING) {

                        throw new IllegalStateException(
                                        "Only SHIPPING orders can be marked as delivered.");
                }

                order.setStatus(
                                OrderStatus.DELIVERED);

                return orderMapper.toResponseDto(
                                orderRepository.save(order));
        }

        @Override
        @Transactional
        public OrderResponseDto processOrder(
                        Long orderId) {

                Order order = getOrder(
                                orderId);

                if (order.getStatus() != OrderStatus.CONFIRMED) {

                        throw new IllegalStateException(
                                        "Only CONFIRMED orders can be processed.");
                }

                order.setStatus(
                                OrderStatus.PROCESSING);

                return orderMapper.toResponseDto(
                                orderRepository.save(order));
        }

        @Override
        @Transactional(readOnly = true)
        public Page<OrderResponseDto> getAllOrders(
                        Pageable pageable) {

                return orderRepository
                                .findAll(pageable)
                                .map(orderMapper::toResponseDto);
        }

        @Override
        @Transactional(readOnly = true)
        public OrderResponseDto getOrderByIdForAdmin(
                        Long orderId) {

                Order order = getOrder(
                                orderId);

                return orderMapper.toResponseDto(order);
        }

        // helper
        private Order getOrder(
                        Long orderId) {

                return orderRepository.findById(orderId)
                                .orElseThrow(() -> new RuntimeException(
                                                "Order not found: " + orderId));
        }

        private void validateCanCancel(
                        Order order) {

                OrderStatus status = order.getStatus();

                if (status != OrderStatus.PENDING
                                && status != OrderStatus.CONFIRMED) {

                        throw new IllegalStateException(
                                        "Order cannot be cancelled from status: "
                                                        + status);
                }
        }

        private BigDecimal calculateShippingFee(
                        CreateOrderRequest request) {

                /*
                 * Implement your actual shipping rule here.
                 *
                 * Temporary:
                 */
                return BigDecimal.ZERO;
        }

        private BigDecimal calculateDiscount(
                        CreateOrderRequest request) {

                /*
                 * Implement your actual discount rule here.
                 *
                 * Temporary:
                 */
                return BigDecimal.ZERO;
        }
}
