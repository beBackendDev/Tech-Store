package net.myapplication.myapp.object.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import net.myapplication.myapp.object.product.entity.LaptopSpecification;

public interface LaptopSpecificationRepository extends JpaRepository<LaptopSpecification, Long> {
    
}
