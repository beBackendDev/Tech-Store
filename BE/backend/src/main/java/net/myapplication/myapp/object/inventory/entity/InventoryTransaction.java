package net.myapplication.myapp.object.inventory.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;

import lombok.*;

import net.myapplication.myapp.object.inventory.enums.InventoryTransactionType;
import net.myapplication.myapp.object.product.entity.Product;

@Entity
@Table(name = "inventory_movements", indexes = {
                @Index(name = "idx_inventory_movement_product", columnList = "product_id"),

                @Index(name = "idx_inventory_movement_created_at", columnList = "created_at")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryTransaction {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        // PRODUCT

        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(name = "product_id", nullable = false)
        private Product product;

        // MOVEMENT TYPE

        @Enumerated(EnumType.STRING)
        @Column(nullable = false, length = 30)
        private InventoryTransactionType type;

        // QUANTITY

        @Column(nullable = false)
        private Integer quantity;

        // STOCK SNAPSHOT

        @Column(nullable = false, name = "stock_before")
        private Integer stockBefore;

        @Column(nullable = false, name = "stock_after")
        private Integer stockAfter;

        // // REFERENCE

        // @Column(length = 100)
        // private String reference;

        // NOTE

        @Column(length = 500)
        private String note;

        // CREATED AT

        @CreationTimestamp
        @Column(nullable = false, updatable = false, name = "created_at")
        private LocalDateTime createdAt;
}