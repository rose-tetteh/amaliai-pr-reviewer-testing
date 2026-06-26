package com.example;

import java.util.HashMap;
import java.util.Map;

public class Inventory {

    private final Map<String, Integer> stock = new HashMap<>();

    public void addStock(String sku, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be positive");
        }
        stock.merge(sku, quantity, Integer::sum);
    }

    public int quantityOf(String sku) {
        return stock.getOrDefault(sku, 0);
    }

    public boolean isInStock(String sku) {
        return quantityOf(sku) > 0;
    }
}
