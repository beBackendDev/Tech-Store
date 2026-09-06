package net.myapplication.myapp.object.inventory.service;

public interface InventoryService {

    void stockIn(
            Long productId,
            Integer quantity,
            String note
    );

    void stockOut(
            Long productId,
            Integer quantity,
            String note
    );

    void reserveStock(
            Long productId,
            Integer quantity,
            String note
    );

    void releaseStock(
            Long productId,
            Integer quantity,
            String note
    );

    void adjustStock(
            Long productId,
            Integer newStock,
            String note
    );

    boolean isAvailable(
            Long productId,
            Integer quantity
    );
}
