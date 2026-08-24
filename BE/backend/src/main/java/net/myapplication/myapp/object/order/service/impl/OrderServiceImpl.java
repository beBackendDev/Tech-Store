package net.myapplication.myapp.object.order.service.impl;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import net.myapplication.myapp.enumpack.OrderStatus;
import net.myapplication.myapp.enumpack.PaymentStatus;
import net.myapplication.myapp.object.order.dto.CreateOrderRequest;
import net.myapplication.myapp.object.order.dto.OrderItemRequest;
import net.myapplication.myapp.object.order.dto.OrderResponseDto;
import net.myapplication.myapp.object.order.entity.Order;
import net.myapplication.myapp.object.order.entity.OrderItem;
import net.myapplication.myapp.object.order.mapper.OrderMapper;
import net.myapplication.myapp.object.order.repository.OrderItemRepository;
import net.myapplication.myapp.object.order.repository.OrderRepository;
import net.myapplication.myapp.object.order.service.OrderService;
import net.myapplication.myapp.object.product.entity.Product;
import net.myapplication.myapp.object.product.repository.ProductRepository;
import net.myapplication.myapp.user.entity.User;
import net.myapplication.myapp.user.repository.UserRepo;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    private final OrderItemRepository orderItemRepository;

    private final ProductRepository productRepository;

    private final UserRepo userRepository;

    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public OrderResponseDto createOrder(CreateOrderRequest request, Long userId) {
        // Find user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
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
        order.setStatus(
                OrderStatus.PENDING);

        // Calculate subtotal
        BigDecimal subtotal = BigDecimal.ZERO;
        for (OrderItemRequest itemRequest : request.getItems()) {
            // validate product existence and availability
            Product product = productRepository
                    .findByIdForUpdate(
                            itemRequest.getProductId())
                    .orElseThrow(() -> new RuntimeException(
                            "Product not found: "
                                    + itemRequest.getProductId()));
            // check Active
            if (!product.isActive()) {
                throw new RuntimeException(
                        "Product is no longer available: "
                                + product.getName());
            }
            // check stock
            Integer requestedQuantity = itemRequest.getQuantity();

            if (product.getStock() < requestedQuantity) {

                throw new RuntimeException(
                        "Not enough stock for product: "
                                + product.getName());
            }
            // current price
            BigDecimal price = product.getPrice();
            // item subtotal
            BigDecimal itemSubtotal = price.multiply(
                    BigDecimal.valueOf(
                            requestedQuantity));

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
                    requestedQuantity);

            orderItem.setSubtotal(
                    itemSubtotal);
            // add to order
            order.addItem(
                    orderItem);
            // decrease product stock
            product.setStock(
                    product.getStock()
                            - requestedQuantity);
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
        return orderMapper.toResponseDto(
                savedOrder);
    }

    // other methods...
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

}
