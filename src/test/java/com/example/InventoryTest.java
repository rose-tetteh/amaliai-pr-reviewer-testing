package com.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class InventoryTest {

    private final Inventory inventory = new Inventory();

    @Test
    void addStockAccumulatesQuantity() {
        inventory.addStock("A1", 3);
        inventory.addStock("A1", 2);
        assertEquals(5, inventory.quantityOf("A1"));
    }

    @Test
    void addStockRejectsNonPositiveQuantity() {
        assertThrows(IllegalArgumentException.class, () -> inventory.addStock("A1", 0));
    }

    @Test
    void isInStockReflectsAvailability() {
        assertFalse(inventory.isInStock("B2"));
        inventory.addStock("B2", 1);
        assertTrue(inventory.isInStock("B2"));
    }
}
