package net.myapplication.myapp.object.inventory.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import net.myapplication.myapp.object.inventory.dto.response.InventoryResponseDto;
import net.myapplication.myapp.object.inventory.dto.response.InventoryTransactionResponseDto;
import net.myapplication.myapp.object.order.entity.Order;

public interface InventoryService {

        void stockIn(
                        Long productId,
                        Integer quantity,
                        String note);

        void stockOut(
                        Long productId,
                        Integer quantity,
                        String note);

        void reserveStock(
                        Long productId,
                        Integer quantity,
                        Order order);

        void commitReservedStock(
                        Long productId,
                        Integer quantity,
                        Order order);

        void releaseStock(
                        Long productId,
                        Integer quantity,
                        String note);

        void releaseReservedStock(
                        Long productId,
                        Integer quantity,
                        Order order,
                        String note);

        void adjustStock(
                        Long productId,
                        Integer newStock,
                        String note);

        boolean isAvailable(
                        Long productId,
                        Integer quantity);

        void returnStock(
                        Long productId,
                        Integer quantity,
                        Order order,
                        String note);
                        
        // =========================================================
        // QUERY
        // =========================================================

        InventoryResponseDto getInventory(
                        Long productId);

        Page<InventoryResponseDto> getInventories(
                        Pageable pageable);

        Page<InventoryTransactionResponseDto> getHistory(
                        Long productId,
                        Pageable pageable);
}
