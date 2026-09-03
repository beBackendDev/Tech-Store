package net.myapplication.myapp.object.product.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
import net.myapplication.myapp.object.product.entity.Product;

@Repository
public interface ProductRepository
                extends JpaRepository<Product, Long>,
                JpaSpecificationExecutor<Product> {

        List<Product> findByActiveTrue();

        Optional<Product> findByIdAndActiveTrue(Long id);

        List<Product> findByCategoryAndActiveTrue(
                        String category);

        // Tránh race condition khi nhiều user cùng mua 1 sản phẩm, cần lock lại để
        // tránh oversell

        @Lock(LockModeType.PESSIMISTIC_WRITE)
        @Query("""
                            SELECT p
                            FROM Product p
                            WHERE p.id = :id
                        """)
        Optional<Product> findByIdForUpdate(
                        @Param("id") Long id);

        // import use externalId
        Optional<Product> findByExternalId(String externalId);
}