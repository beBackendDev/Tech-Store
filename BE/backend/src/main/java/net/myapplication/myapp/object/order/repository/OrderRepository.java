package net.myapplication.myapp.object.order.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import net.myapplication.myapp.enumpack.OrderStatus;
import net.myapplication.myapp.enumpack.PaymentStatus;
import net.myapplication.myapp.object.order.entity.Order;
import net.myapplication.myapp.object.order.repository.projection.DailyOrderProjection;
import net.myapplication.myapp.object.order.repository.projection.DailyRevenueProjection;
import net.myapplication.myapp.user.entity.User;

public interface OrderRepository
        extends JpaRepository<Order, Long>,
        JpaSpecificationExecutor<Order> {

    // order history
    List<Order> findByUserOrderByCreatedAtDesc(User user);

    @Query("""
                SELECT o
                FROM Order o
                WHERE o.user.id = :userId
                ORDER BY o.createdAt DESC
            """)
    List<Order> findOrdersByUserId(
            @Param("userId") Long userId);

    // query to find order by id and user id & order id
    @Query("""
                SELECT o
                FROM Order o
                WHERE o.id = :orderId
                AND o.user.id = :userId
            """)
    Optional<Order> findByIdAndUserId(
            @Param("orderId") Long orderId,
            @Param("userId") Long userId);

    @Query("""
                SELECT o
                FROM Order o
                WHERE o.user.id = :userId
                ORDER BY o.createdAt DESC
            """)
    Page<Order> findByUserId(
            @Param("userId") Long userId,
            Pageable pageable);

    Page<Order> findByStatus(
            @Param("status") OrderStatus status,
            Pageable pageable);

    /*
     * ============================DASHBOARD=============================
     */
    long countByStatus(OrderStatus status);

    long countByPaymentStatus(PaymentStatus paymentStatus);

    // REVENUE
    @Query("""
                SELECT COALESCE(SUM(o.totalAmount), 0)
                FROM Order o
                WHERE o.paymentStatus = :paymentStatus
            """)
    BigDecimal sumRevenueByPaymentStatus(
            @Param("paymentStatus") PaymentStatus paymentStatus);
    /*
     * RECENT ORDERS
     * 
     * Không dùng findAll().
     * 
     * Pageable sẽ giới hạn số record lấy từ DB.
     */

    List<Order> findByOrderByCreatedAtDesc(
            Pageable pageable);

    /*
     * REVENUE CHART
     *
     * MySQL:
     *
     * DATE(created_at)
     *
     * group revenue theo từng ngày.
     */

    @Query(value = """
            SELECT
                DATE(o.created_at) AS date,
                COALESCE(SUM(o.total_amount), 0) AS revenue

            FROM orders o

            WHERE o.payment_status = :paymentStatus
              AND o.created_at >= :start
              AND o.created_at < :end

            GROUP BY DATE(o.created_at)

            ORDER BY DATE(o.created_at)
            """, nativeQuery = true)
    List<DailyRevenueProjection> findDailyRevenue(
            @Param("paymentStatus") String paymentStatus,

            @Param("start") LocalDateTime start,

            @Param("end") LocalDateTime end);

    /*
     * =========================================================
     * ORDER CHART
     * =========================================================
     */

    @Query(value = """
            SELECT
                DATE(o.created_at) AS date,
                COUNT(*) AS orders

            FROM orders o

            WHERE o.created_at >= :start
              AND o.created_at < :end

            GROUP BY DATE(o.created_at)

            ORDER BY DATE(o.created_at)
            """, nativeQuery = true)
    List<DailyOrderProjection> findDailyOrders(
            @Param("start") LocalDateTime start,

            @Param("end") LocalDateTime end);
}