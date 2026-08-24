package net.myapplication.myapp.object.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import net.myapplication.myapp.object.order.entity.Order;
import net.myapplication.myapp.user.entity.User;

public interface OrderRepository
        extends JpaRepository<Order, Long> {

    List<Order> findByUserOrderByCreatedAtDesc(User user);

}