package net.myapplication.myapp.object.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import net.myapplication.myapp.object.order.entity.OrderItem;

public interface OrderItemRepository
        extends JpaRepository<OrderItem, Long> {
}
