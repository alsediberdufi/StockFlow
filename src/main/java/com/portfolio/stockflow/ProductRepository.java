package com.portfolio.stockflow;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategoryIgnoreCase(String category);

    List<Product> findByStockLessThanEqual(int stock);

    List<Product> findByCategoryIgnoreCaseAndStockLessThanEqual(String category, int stock);

    long countByStockLessThanEqual(int stock);
}
