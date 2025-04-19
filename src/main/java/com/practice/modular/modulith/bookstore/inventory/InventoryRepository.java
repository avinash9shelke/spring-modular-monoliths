package com.practice.modular.modulith.bookstore.inventory;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author avinash
 */
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
}
