package net.myapplication.myapp.object.product.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "laptop_specifications")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LaptopSpecification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "product_id",
        nullable = false,
        unique = true
    )
    private Product product;

    @Column(nullable = false, length = 100)
    private String brand;

    @Column(nullable = false, length = 150)
    private String processor;

    @Column(nullable = false)
    private Integer ram;

    @Column(nullable = false)
    private Integer ssd;

    @Column(nullable = false)
    private Integer hardDisk;

    @Column(nullable = false, length = 100)
    private String operatingSystem;

    @Column(length = 150)
    private String graphics;

    @Column(precision = 4, scale = 1)
    private BigDecimal screenSize;

    @Column(length = 50)
    private String resolution;
}