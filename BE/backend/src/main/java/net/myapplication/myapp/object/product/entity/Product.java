package net.myapplication.myapp.object.product.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.opencsv.bean.CsvBindByName;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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

    @Column(nullable = false)
    private Integer stock; // stock

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
}