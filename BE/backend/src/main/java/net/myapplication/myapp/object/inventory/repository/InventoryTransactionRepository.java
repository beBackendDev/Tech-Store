package net.myapplication.myapp.object.inventory.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import net.myapplication.myapp.object.inventory.entity.InventoryTransaction;

public interface InventoryTransactionRepository
                extends JpaRepository<InventoryTransaction, Long> {
        List<InventoryTransaction> findByProductIdOrderByCreatedAtDesc(
                        Long productId);

        List<InventoryTransaction> findByOrderIdOrderByCreatedAtAsc(
                        Long orderId);

        Page<InventoryTransaction> findByProductId(
                        Long productId,
                        Pageable pageable);
}
