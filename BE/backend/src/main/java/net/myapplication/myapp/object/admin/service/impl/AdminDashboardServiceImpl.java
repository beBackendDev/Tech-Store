package net.myapplication.myapp.object.admin.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import net.myapplication.myapp.enumpack.OrderStatus;
import net.myapplication.myapp.enumpack.PaymentStatus;
import net.myapplication.myapp.object.admin.dto.dashboard.AdminDashboardResponseDto;
import net.myapplication.myapp.object.admin.dto.dashboard.DashboardOverviewDto;
import net.myapplication.myapp.object.admin.dto.dashboard.InventoryStatisticDto;
import net.myapplication.myapp.object.admin.dto.dashboard.LowStockProductDto;
import net.myapplication.myapp.object.admin.dto.dashboard.OrderStatisticDto;
import net.myapplication.myapp.object.admin.dto.dashboard.ProductStatisticDto;
import net.myapplication.myapp.object.admin.dto.dashboard.RecentOrderDto;
import net.myapplication.myapp.object.admin.dto.dashboard.analytics.RevenueChartDto;
import net.myapplication.myapp.object.admin.service.AdminDashboardService;
import net.myapplication.myapp.object.inventory.enums.InventoryTransactionType;
import net.myapplication.myapp.object.inventory.repository.InventoryTransactionRepository;
import net.myapplication.myapp.object.order.entity.Order;
import net.myapplication.myapp.object.order.repository.OrderRepository;
import net.myapplication.myapp.object.product.entity.Product;
import net.myapplication.myapp.object.product.repository.ProductRepository;
import net.myapplication.myapp.user.repository.UserRepo;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminDashboardServiceImpl implements AdminDashboardService {
    private final OrderRepository orderRepository;

    private final ProductRepository productRepository;

    private final InventoryTransactionRepository inventoryTransactionRepository;

    private final UserRepo userRepository;

    private static final Integer LOW_STOCK_THRESHOLD = 10;

    private static final int RECENT_ORDER_LIMIT = 5;

    private static final int LOW_STOCK_PRODUCT_LIMIT = 5;

    @Override
    public AdminDashboardResponseDto getDashboard() {

        DashboardOverviewDto overview = buildOverview();

        OrderStatisticDto orderStatistics = buildOrderStatistics();

        ProductStatisticDto productStatistics = buildProductStatistics();

        InventoryStatisticDto inventoryStatistics = buildInventoryStatistics();

        List<RecentOrderDto> recentOrders = getRecentOrders();

        List<LowStockProductDto> lowStockProducts = getLowStockProducts();

        return AdminDashboardResponseDto.builder()

                .overview(overview)

                .orderStatistics(orderStatistics)

                .productStatistics(productStatistics)

                .inventoryStatistics(inventoryStatistics)

                .recentOrders(recentOrders)

                .lowStockProducts(lowStockProducts)

                .build();
    }

    // other methods
    private DashboardOverviewDto buildOverview() {

        Long totalOrders = orderRepository.count();

        Long totalProducts = productRepository.count();

        Long totalCustomers = userRepository.count();

        var totalRevenue = orderRepository.sumRevenueByPaymentStatus(
                PaymentStatus.PAID);

        return DashboardOverviewDto.builder()

                .totalRevenue(
                        totalRevenue)

                .totalOrders(
                        totalOrders)

                .totalProducts(
                        totalProducts)

                .totalCustomers(
                        totalCustomers)

                .build();
    }

    private OrderStatisticDto buildOrderStatistics() {

        return OrderStatisticDto.builder()

                .totalOrders(
                        orderRepository.count())

                .pendingOrders(
                        orderRepository.countByStatus(
                                OrderStatus.PENDING))

                .confirmedOrders(
                        orderRepository.countByStatus(
                                OrderStatus.CONFIRMED))

                .processingOrders(
                        orderRepository.countByStatus(
                                OrderStatus.PROCESSING))

                .shippedOrders(
                        orderRepository.countByStatus(
                                OrderStatus.COMPLETED))

                .deliveredOrders(
                        orderRepository.countByStatus(
                                OrderStatus.DELIVERED))

                .cancelledOrders(
                        orderRepository.countByStatus(
                                OrderStatus.CANCELLED))

                .returnRequests(
                        orderRepository.countByStatus(
                                OrderStatus.RETURN_REQUESTED))

                .returnedOrders(
                        orderRepository.countByStatus(
                                OrderStatus.RETURNED))

                .build();
    }

    private ProductStatisticDto buildProductStatistics() {

        Long total = productRepository.count();

        Long active = productRepository.countByActive(true);

        Long inactive = productRepository.countByActive(false);

        Long outOfStock = productRepository.countOutOfStock();

        Long lowStock = productRepository.countLowStock(
                LOW_STOCK_THRESHOLD);

        Long totalStock = productRepository.sumTotalStock();

        return ProductStatisticDto.builder()

                .totalProducts(total)

                .activeProducts(active)

                .inactiveProducts(inactive)

                .outOfStockProducts(outOfStock)

                .lowStockProducts(lowStock)

                .totalStock(totalStock)

                .build();
    }

    // inventory statistics
    private Long nullToZero(Long value) {

        return value == null
                ? 0L
                : value;
    }

    private InventoryStatisticDto buildInventoryStatistics() {

        LocalDateTime startOfDay = LocalDateTime.now()
                .with(LocalTime.MIN);

        LocalDateTime now = LocalDateTime.now();

        Long stockIn = inventoryTransactionRepository
                .sumQuantityByTypeAndCreatedAtBetween(
                        InventoryTransactionType.STOCK_IN,
                        startOfDay,
                        now);

        Long stockOut = inventoryTransactionRepository
                .sumQuantityByTypeAndCreatedAtBetween(
                        InventoryTransactionType.STOCK_OUT,
                        startOfDay,
                        now);

        Long reserved = inventoryTransactionRepository
                .sumQuantityByTypeAndCreatedAtBetween(
                        InventoryTransactionType.RESERVED,
                        startOfDay,
                        now);

        Long released = inventoryTransactionRepository
                .sumQuantityByTypeAndCreatedAtBetween(
                        InventoryTransactionType.RELEASED,
                        startOfDay,
                        now);

        Long adjustment = inventoryTransactionRepository
                .sumQuantityByTypeAndCreatedAtBetween(
                        InventoryTransactionType.ADJUSTMENT,
                        startOfDay,
                        now);

        return InventoryStatisticDto.builder()

                .stockInToday(
                        nullToZero(stockIn))

                .stockOutToday(
                        nullToZero(stockOut))

                .reservedToday(
                        nullToZero(reserved))

                .releasedToday(
                        nullToZero(released))

                .adjustmentToday(
                        nullToZero(adjustment))

                .build();
    }

    private List<RecentOrderDto> getRecentOrders() {

        Pageable pageable = PageRequest.of(
                0,
                RECENT_ORDER_LIMIT);

        return orderRepository
                .findByOrderByCreatedAtDesc(pageable)
                .stream()
                .map(this::mapToRecentOrderDto)
                .toList();
    }

    private List<LowStockProductDto> getLowStockProducts() {

        Pageable pageable = PageRequest.of(
                0,
                LOW_STOCK_PRODUCT_LIMIT);

        return productRepository
                .findLowStockProducts(
                        LOW_STOCK_THRESHOLD,
                        pageable)
                .stream()
                .map(this::mapToLowStockProductDto)
                .toList();
    }

    // mapper
    private RecentOrderDto mapToRecentOrderDto(
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

    private LowStockProductDto mapToLowStockProductDto(
            Product product) {

        return LowStockProductDto.builder()

                .productId(
                        product.getId())

                .productName(
                        product.getName())

                .image(
                        product.getImage())

                .stock(
                        product.getStock())

                .build();
    }

    // helper
  
}