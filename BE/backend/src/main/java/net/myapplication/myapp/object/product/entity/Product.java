package net.myapplication.myapp.object.product.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "products", uniqueConstraints = {
        @UniqueConstraint(name = "uk_products_external_id", columnNames = "external_id") })
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "product", fetch = FetchType.LAZY)
    private LaptopSpecification laptopSpecification;

    @Column(nullable = false, length = 255)
    private String name; // title

    @Column(nullable = true, length = 255)
    private String description; // description

    @Column(nullable = false, length = 100)
    private String category; // category

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal price;

    @Column(precision = 15, scale = 2)
    private BigDecimal oldPrice;// price

    // @Version
    // private Long version;

    @Column(nullable = false)
    private Integer stock; // stock

    @Builder.Default
    @Column(nullable = false)
    private Integer reservedStock = 0;

    @Column(nullable = true, length = 500)
    private String image;

    @Column(precision = 2, scale = 1)
    private BigDecimal rating; // rating

    @Column(nullable = false)
    private Integer reviewCount;

    @Column(nullable = false)
    private boolean isNew;

    @Column(nullable = false)
    private boolean active; // availabilityStatus

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {

        LocalDateTime now = LocalDateTime.now();

        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {

        updatedAt = LocalDateTime.now();
    }

    // ho tro trong pipeline data tu dataset
    @Column(
            // nullable = false,/* */
            nullable = false, unique = true, length = 50)
    private String externalId;

    // INVENTORY METHODS
    public int getAvailableStock() {

        return stock - reservedStock;
    }

    public boolean hasAvailableStock(
            int quantity) {

        return getAvailableStock() >= quantity;
    }

    public void reserveStock(
            Integer quantity) {

        validateQuantity(quantity);

        if (getAvailableStock() < quantity) {

            throw new IllegalStateException(
                    "Insufficient available stock");
        }

        reservedStock += quantity;
    }

    public void releaseReservedStock(
            Integer quantity) {

        validateQuantity(quantity);

        if (reservedStock < quantity) {

            throw new IllegalStateException(
                    "Cannot release more than reserved stock");
        }

        reservedStock -= quantity;
    }

    public void commitReservedStock(
            Integer quantity) {

        validateQuantity(quantity);

        if (reservedStock < quantity) {

            throw new IllegalStateException(
                    "Insufficient reserved stock");
        }

        stock -= quantity;

        reservedStock -= quantity;
    }

    public void increaseStock(
            Integer quantity) {

        validateQuantity(quantity);

        stock += quantity;
    }

    public void adjustStock(
            Integer quantity) {

        if (quantity == null || quantity == 0) {

            throw new IllegalArgumentException(
                    "Adjustment quantity cannot be zero");
        }

        int newStock = stock + quantity;

        if (newStock < 0) {

            throw new IllegalStateException(
                    "Stock cannot be negative");
        }

        if (newStock < reservedStock) {

            throw new IllegalStateException(
                    "Stock cannot be lower than reserved stock");
        }

        stock = newStock;
    }

    private void validateQuantity(
            Integer quantity) {

        if (quantity == null || quantity <= 0) {

            throw new IllegalArgumentException(
                    "Quantity must be greater than zero");
        }
    }
}
