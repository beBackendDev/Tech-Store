package net.myapplication.myapp.object.product.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.boot.data.autoconfigure.web.DataWebProperties.Pageable;
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

        /*
         * =========================DASHBOARD================================
         */

        // PRODUCT COUNT

        long countByActive(boolean active);

        // TOTAL PHYSICAL STOCK

        @Query("""
                            SELECT COALESCE(SUM(p.stock), 0)
                            FROM Product p
                        """)
        Long sumTotalStock();

        // OUT OF STOCK

        @Query("""
                            SELECT COUNT(p)
                            FROM Product p
                            WHERE p.stock <= 0
                        """)
        long countOutOfStock();

        // LOW STOCK
        // threshold được truyền từ Service.

        @Query("""
                            SELECT COUNT(p)
                            FROM Product p
                            WHERE p.stock > 0
                              AND p.stock <= :threshold
                        """)
        long countLowStock(
                        @Param("threshold") Integer threshold);

        // LOW STOCK PRODUCTS
        // Không lấy toàn bộ product.
        // Ví dụ Dashboard chỉ cần 5 sản phẩm cảnh báo.

        @Query("""
                            SELECT p
                            FROM Product p
                            WHERE p.stock <= :threshold
                            ORDER BY p.stock ASC
                        """)
        List<Product> findLowStockProducts(
                        @Param("threshold") Integer threshold,

                        Pageable pageable);
}