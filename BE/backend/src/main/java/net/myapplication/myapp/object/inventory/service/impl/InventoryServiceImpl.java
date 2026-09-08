package net.myapplication.myapp.object.inventory.service.impl;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.myapplication.myapp.object.inventory.entity.InventoryTransaction;
import net.myapplication.myapp.object.inventory.enums.InventoryTransactionType;
import net.myapplication.myapp.object.inventory.repository.InventoryTransactionRepository;
import net.myapplication.myapp.object.inventory.service.InventoryService;
import net.myapplication.myapp.object.order.entity.Order;
import net.myapplication.myapp.object.product.entity.Product;
import net.myapplication.myapp.object.product.repository.ProductRepository;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl
        implements InventoryService {

    private final ProductRepository productRepository;

    private final InventoryTransactionRepository
            inventoryTransactionRepository;


    // =========================================================
    // STOCK IN
    // =========================================================

    @Override
    @Transactional
    public void stockIn(
            Long productId,
            Integer quantity,
            String note) {

        validatePositiveQuantity(quantity);

        Product product =
                getProductForUpdate(productId);

        int stockBefore =
                product.getStock();

        int reservedBefore =
                product.getReservedStock();

        product.increaseStock(quantity);

        createTransaction(
                product,
                null,
                InventoryTransactionType.STOCK_IN,
                quantity,
                stockBefore,
                product.getStock(),
                reservedBefore,
                product.getReservedStock(),
                note
        );
    }


    // =========================================================
    // RESERVE STOCK
    // =========================================================

    @Override
    @Transactional
    public void reserveStock(
            Long productId,
            Integer quantity,
            Order order) {

        validatePositiveQuantity(quantity);

        Product product =
                getProductForUpdate(productId);

        int stockBefore =
                product.getStock();

        int reservedBefore =
                product.getReservedStock();

        product.reserveStock(quantity);

        createTransaction(
                product,
                order,
                InventoryTransactionType.RESERVED,
                quantity,
                stockBefore,
                product.getStock(),
                reservedBefore,
                product.getReservedStock(),
                "Stock reserved for order"
        );
    }


    // =========================================================
    // RELEASE RESERVED STOCK
    // =========================================================

    @Override
    @Transactional
    public void releaseReservedStock(
            Long productId,
            Integer quantity,
            Order order,
            String note) {

        validatePositiveQuantity(quantity);

        Product product =
                getProductForUpdate(productId);

        int stockBefore =
                product.getStock();

        int reservedBefore =
                product.getReservedStock();

        product.releaseReservedStock(quantity);

        createTransaction(
                product,
                order,
                InventoryTransactionType.RELEASED,
                quantity,
                stockBefore,
                product.getStock(),
                reservedBefore,
                product.getReservedStock(),
                note
        );
    }


    // =========================================================
    // COMMIT RESERVED STOCK
    // =========================================================

    @Override
    @Transactional
    public void commitReservedStock(
            Long productId,
            Integer quantity,
            Order order) {

        validatePositiveQuantity(quantity);

        Product product =
                getProductForUpdate(productId);

        int stockBefore =
                product.getStock();

        int reservedBefore =
                product.getReservedStock();

        product.commitReservedStock(quantity);

        createTransaction(
                product,
                order,
                InventoryTransactionType.STOCK_OUT,
                quantity,
                stockBefore,
                product.getStock(),
                reservedBefore,
                product.getReservedStock(),
                "Reserved stock committed for order"
        );
    }


    // =========================================================
    // ADJUST STOCK
    // =========================================================

    @Override
    @Transactional
    public void adjustStock(
            Long productId,
            Integer quantity,
            String note) {

        if (quantity == null ||
                quantity == 0) {

            throw new IllegalArgumentException(
                    "Adjustment quantity cannot be zero"
            );
        }

        Product product =
                getProductForUpdate(productId);

        int stockBefore =
                product.getStock();

        int reservedBefore =
                product.getReservedStock();

        product.adjustStock(quantity);

        createTransaction(
                product,
                null,
                InventoryTransactionType.ADJUSTMENT,
                quantity,
                stockBefore,
                product.getStock(),
                reservedBefore,
                product.getReservedStock(),
                note
        );
    }


    // =========================================================
    // RETURN STOCK
    // =========================================================

    @Override
    @Transactional
    public void returnStock(
            Long productId,
            Integer quantity,
            Order order,
            String note) {

        validatePositiveQuantity(quantity);

        Product product =
                getProductForUpdate(productId);

        int stockBefore =
                product.getStock();

        int reservedBefore =
                product.getReservedStock();

        product.increaseStock(quantity);

        createTransaction(
                product,
                order,
                InventoryTransactionType.RETURNED,
                quantity,
                stockBefore,
                product.getStock(),
                reservedBefore,
                product.getReservedStock(),
                note
        );
    }


    // =========================================================
    // CHECK AVAILABILITY
    // =========================================================

    @Override
    @Transactional
    public boolean isAvailable(
            Long productId,
            Integer quantity) {

        validatePositiveQuantity(quantity);

        Product product =
                productRepository.findById(productId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Product not found: "
                                                + productId
                                )
                        );

        return product.getAvailableStock()
                >= quantity;
    }


    // =========================================================
    // GET PRODUCT FOR UPDATE
    // =========================================================

    private Product getProductForUpdate(
            Long productId) {

        return productRepository
                .findByIdForUpdate(productId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Product not found: "
                                        + productId
                        )
                );
    }


    // =========================================================
    // CREATE TRANSACTION
    // =========================================================

    private void createTransaction(
            Product product,
            Order order,
            InventoryTransactionType type,
            Integer quantity,
            Integer stockBefore,
            Integer stockAfter,
            Integer reservedBefore,
            Integer reservedAfter,
            String note) {

        InventoryTransaction transaction =
                InventoryTransaction.builder()

                        .product(product)

                        .order(order)

                        .type(type)

                        .quantity(quantity)

                        .stockBefore(stockBefore)

                        .stockAfter(stockAfter)

                        .reservedBefore(
                                reservedBefore
                        )

                        .reservedAfter(
                                reservedAfter
                        )

                        .note(note)

                        .build();

        inventoryTransactionRepository.save(
                transaction
        );
    }


    // =========================================================
    // VALIDATION
    // =========================================================

    private void validatePositiveQuantity(
            Integer quantity) {

        if (quantity == null ||
                quantity <= 0) {

            throw new IllegalArgumentException(
                    "Quantity must be greater than zero"
            );
        }
    }


    @Override
    public void releaseStock(Long productId, Integer quantity, String note) {
        // TODO Auto-generated method stub
        
    }


    @Override
    public void stockOut(Long productId, Integer quantity, String note) {
        // TODO Auto-generated method stub
        
    }
}