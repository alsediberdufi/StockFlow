package com.portfolio.stockflow;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductTest {

    @Test
    void reserveStockReducesStock() {
        Product product = new Product("Mouse", "Accessories", BigDecimal.TEN, 5);

        product.reserveStock(2);

        assertEquals(3, product.getStock());
    }

    @Test
    void reserveStockFailsWhenThereIsNotEnoughStock() {
        Product product = new Product("Mouse", "Accessories", BigDecimal.TEN, 1);

        assertThrows(BusinessException.class, () -> product.reserveStock(2));
    }

    @Test
    void restockAddsToCurrentStock() {
        Product product = new Product("Mouse", "Accessories", BigDecimal.TEN, 4);

        product.restock(6);

        assertEquals(10, product.getStock());
    }
}
