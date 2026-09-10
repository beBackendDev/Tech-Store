package net.myapplication.myapp.object.order.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import net.myapplication.myapp.enumpack.OrderStatus;
import net.myapplication.myapp.object.order.entity.Order;
import net.myapplication.myapp.user.entity.User;

public interface OrderRepository
                extends JpaRepository<Order, Long>,
                JpaSpecificationExecutor<Order>  {

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
}