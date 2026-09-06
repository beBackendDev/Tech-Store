package net.myapplication.myapp.object.inventory.service.impl;

import net.myapplication.myapp.object.inventory.service.InventoryService;

public class InventoryServiceImpl implements InventoryService {

    @Override
    public void adjustStock(Long productId, Integer newStock, String note) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean isAvailable(Long productId, Integer quantity) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void releaseStock(Long productId, Integer quantity, String note) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void reserveStock(Long productId, Integer quantity, String note) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void stockIn(Long productId, Integer quantity, String note) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void stockOut(Long productId, Integer quantity, String note) {
        // TODO Auto-generated method stub
        
    }

}
