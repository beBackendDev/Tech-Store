package net.myapplication.myapp.object.inventory.service;

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

        public void reserveStock(
                        Long productId,
                        Integer quantity,
                        Order order);

        public void commitReservedStock(
                        Long productId,
                        Integer quantity,
                        Order order);

        void releaseStock(
                        Long productId,
                        Integer quantity,
                        String note);

        public void releaseReservedStock(
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

        public void returnStock(
                        Long productId,
                        Integer quantity,
                        Order order,
                        String note);
}
