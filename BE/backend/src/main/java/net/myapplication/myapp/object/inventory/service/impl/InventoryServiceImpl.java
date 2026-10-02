package net.myapplication.myapp.object.inventory.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.myapplication.myapp.object.inventory.constants.InventoryConstants;
import net.myapplication.myapp.object.inventory.dto.response.InventoryResponseDto;
import net.myapplication.myapp.object.inventory.dto.response.InventoryTransactionResponseDto;
import net.myapplication.myapp.object.inventory.entity.InventoryTransaction;
import net.myapplication.myapp.object.inventory.enums.InventoryStockStatus;
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

        private final InventoryTransactionRepository inventoryTransactionRepository;

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

                Product product = getProductForUpdate(productId);

                int stockBefore = product.getStock();

                int reservedBefore = product.getReservedStock();

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
                                note);
        }

        @Transactional
        @Override
        public void stockOut(
                        Long productId,
                        Integer quantity,
                        String note) {

                Product product = getProductForUpdate(productId);

                if (!product.hasAvailableStock(quantity)) {
                        throw new RuntimeException(
                                        "Insufficient available stock. " +
                                                        "Available: " +
                                                        product.getAvailableStock() +
                                                        ", requested: " +
                                                        quantity);
                }

                int stockBefore = product.getStock();

                int reservedBefore = product.getReservedStock();

                product.adjustStock(-quantity);

                createTransaction(
                                product,
                                null,
                                InventoryTransactionType.STOCK_OUT,
                                quantity,
                                stockBefore,
                                product.getStock(),
                                reservedBefore,
                                product.getReservedStock(),
                                note);
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

                Product product = getProductForUpdate(productId);

                int stockBefore = product.getStock();

                int reservedBefore = product.getReservedStock();

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
                                "Stock reserved for order");
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

                Product product = getProductForUpdate(productId);

                int stockBefore = product.getStock();

                int reservedBefore = product.getReservedStock();

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
                                note);
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

                Product product = getProductForUpdate(productId);

                int stockBefore = product.getStock();

                int reservedBefore = product.getReservedStock();

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
                                "Reserved stock committed for order");
        }

        // =========================================================
        // ADJUST STOCK
        // =========================================================

        @Override
        @Transactional
        public void adjustStock(
                        Long productId,
                        Integer actualStock,
                        String note) {

                if (actualStock == null ||
                                actualStock == 0) {

                        throw new IllegalArgumentException(
                                        "Adjustment quantity cannot be zero");
                }

                Product product = getProductForUpdate(productId);

                int stockBefore = product.getStock();

                int reservedBefore = product.getReservedStock();

                int difference = actualStock - stockBefore;

                if(difference == 0 ){
                        return ;
                }

                product.adjustStock(difference);

                createTransaction(
                                product,
                                null,
                                InventoryTransactionType.ADJUSTMENT,
                                Math.abs(difference),
                                stockBefore,
                                product.getStock(),
                                reservedBefore,
                                product.getReservedStock(),
                                note);
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

                Product product = getProductForUpdate(productId);

                int stockBefore = product.getStock();

                int reservedBefore = product.getReservedStock();

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
                                note);
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

                Product product = productRepository.findById(productId)
                                .orElseThrow(() -> new RuntimeException(
                                                "Product not found: "
                                                                + productId));

                return product.getAvailableStock() >= quantity;
        }

        // =========================================================
        // GET PRODUCT FOR UPDATE
        // =========================================================

        private Product getProductForUpdate(
                        Long productId) {

                return productRepository
                                .findByIdForUpdate(productId)
                                .orElseThrow(() -> new RuntimeException(
                                                "Product not found: "
                                                                + productId));
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

                InventoryTransaction transaction = InventoryTransaction.builder()

                                .product(product)

                                .order(order)

                                .type(type)

                                .quantity(quantity)

                                .stockBefore(stockBefore)

                                .stockAfter(stockAfter)

                                .reservedBefore(
                                                reservedBefore)

                                .reservedAfter(
                                                reservedAfter)

                                .note(note)

                                .build();

                inventoryTransactionRepository.save(
                                transaction);
        }

        // =========================================================
        // VALIDATION
        // =========================================================

        private void validatePositiveQuantity(
                        Integer quantity) {

                if (quantity == null ||
                                quantity <= 0) {

                        throw new IllegalArgumentException(
                                        "Quantity must be greater than zero");
                }
        }

        @Override
        public void releaseStock(Long productId, Integer quantity, String note) {
                // TODO Auto-generated method stub

        }

        // INVENTORY QUERY

        @Override
        public Page<InventoryTransactionResponseDto> getHistory(Long productId, Pageable pageable) {
                return inventoryTransactionRepository
                                .findByProductId(
                                                productId,
                                                pageable)
                                .map(this::toTransactionResponse);
        }

        @Override
        public Page<InventoryResponseDto> getInventories(Pageable pageable) {
                return productRepository
                                .findAll(pageable)
                                .map(this::toInventoryResponse);
        }

        @Override
        public InventoryResponseDto getInventory(Long productId) {
                Product product = productRepository.findById(productId)
                                .orElseThrow(() -> new RuntimeException(
                                                "Product not found"));

                return toInventoryResponse(product);
        }
        // =========================================================
        // RESPONSE MAPPER
        // =========================================================

        private InventoryResponseDto toInventoryResponse(
                        Product product) {

                return InventoryResponseDto.builder()
                                .productId(product.getId())
                                .externalId(product.getExternalId())
                                .productName(product.getName())
                                .category(product.getCategory())
                                .image(product.getImage())
                                .stock(product.getStock())
                                .reservedStock(product.getReservedStock())
                                .availableStock(
                                                product.getAvailableStock())
                                .status(
                                                resolveStockStatus(
                                                                product.getAvailableStock()))
                                .build();
        }

        private InventoryTransactionResponseDto toTransactionResponse(
                        InventoryTransaction transaction) {

                return InventoryTransactionResponseDto.builder()
                                .id(transaction.getId())
                                .productId(
                                                transaction.getProduct().getId())
                                .productName(
                                                transaction.getProduct().getName())
                                .orderId(
                                                transaction.getOrder() != null
                                                                ? transaction.getOrder().getId()
                                                                : null)
                                .type(transaction.getType())
                                .quantity(transaction.getQuantity())
                                .stockBefore(transaction.getStockBefore())
                                .stockAfter(transaction.getStockAfter())
                                .reservedBefore(
                                                transaction.getReservedBefore())
                                .reservedAfter(
                                                transaction.getReservedAfter())
                                .note(transaction.getNote())
                                .createdAt(transaction.getCreatedAt())
                                .build();
        }
        // =========================================================
        // STOCK STATUS
        // =========================================================

        private InventoryStockStatus resolveStockStatus(
                        int availableStock) {

                if (availableStock <= 0) {

                        return InventoryStockStatus.OUT_OF_STOCK;
                }

                if (availableStock <= InventoryConstants.LOW_STOCK_THRESHOLD) {

                        return InventoryStockStatus.LOW_STOCK;
                }

                return InventoryStockStatus.IN_STOCK;
        }

}