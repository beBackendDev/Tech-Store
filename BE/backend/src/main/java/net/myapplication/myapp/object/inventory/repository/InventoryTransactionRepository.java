package net.myapplication.myapp.object.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import net.myapplication.myapp.object.inventory.entity.InventoryTransaction;

public interface InventoryTransactionRepository
        extends JpaRepository<InventoryTransaction, Long> {

}
