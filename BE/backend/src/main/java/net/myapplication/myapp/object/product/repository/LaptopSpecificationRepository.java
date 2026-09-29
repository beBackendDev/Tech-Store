package net.myapplication.myapp.object.product.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import net.myapplication.myapp.object.product.entity.LaptopSpecification;

public interface LaptopSpecificationRepository
        extends JpaRepository<LaptopSpecification, Long> {
    Optional<LaptopSpecification> findByProductId(
            Long productId);
}
