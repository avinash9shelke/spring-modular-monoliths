package com.practice.modular.modulith.bookstore.inventory;

import org.jmolecules.event.annotation.DomainEvent;

/**
 * @author avinash
 */
@DomainEvent
public record StockAddedEvent(Long inventoryId, int quantityAdded) {
}
