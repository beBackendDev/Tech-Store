package net.myapplication.myapp.object.inventory.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import net.myapplication.myapp.object.inventory.entity.InventoryTransaction;
import net.myapplication.myapp.object.inventory.enums.InventoryTransactionType;
import net.myapplication.myapp.object.inventory.repository.projection.DailyInventoryMovementProjection;

public interface InventoryTransactionRepository
                extends JpaRepository<InventoryTransaction, Long> {
        List<InventoryTransaction> findByProductIdOrderByCreatedAtDesc(
                        Long productId);

        List<InventoryTransaction> findByOrderIdOrderByCreatedAtAsc(
                        Long orderId);

        Page<InventoryTransaction> findByProductId(
                        Long productId,
                        Pageable pageable);
        /*
         * =========================DASHBOARD================================
         */
        /*
         * =========================================================
         * TOTAL QUANTITY BY MOVEMENT TYPE
         * =========================================================
         */

        @Query("""
                            SELECT COALESCE(SUM(i.quantity), 0)
                            FROM InventoryTransaction i
                            WHERE i.type = :type
                        """)
        Long sumQuantityByType(
                        @Param("type") InventoryTransactionType type);

        /*
         * =========================================================
         * QUANTITY BY TYPE + TIME RANGE
         * =========================================================
         *
         * Dashboard:
         *
         * Stock In today
         * Stock Out today
         * Reserved today
         * Released today
         */

        @Query("""
                            SELECT COALESCE(SUM(i.quantity), 0)
                            FROM InventoryTransaction i
                            WHERE i.type = :type
                              AND i.createdAt >= :start
                              AND i.createdAt < :end
                        """)
        Long sumQuantityByTypeAndCreatedAtBetween(
                        @Param("type") InventoryTransactionType type,

                        @Param("start") LocalDateTime start,

                        @Param("end") LocalDateTime end);

        //
        @Query(value = """
                        SELECT
                            DATE(i.created_at) AS date,
                            COALESCE(SUM(i.quantity), 0) AS quantity

                        FROM inventory_movements i

                        WHERE i.type = :type
                          AND i.created_at >= :start
                          AND i.created_at < :end

                        GROUP BY DATE(i.created_at)

                        ORDER BY DATE(i.created_at)
                        """, nativeQuery = true)
        List<DailyInventoryMovementProjection> findDailyMovement(
                        @Param("type") String type,

                        @Param("start") LocalDateTime start,

                        @Param("end") LocalDateTime end);
}
