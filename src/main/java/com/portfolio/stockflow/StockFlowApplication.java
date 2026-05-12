package com.portfolio.stockflow;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.util.List;

@SpringBootApplication
public class StockFlowApplication {

    public static void main(String[] args) {
        SpringApplication.run(StockFlowApplication.class, args);
    }

    @Bean
    CommandLineRunner loadProducts(ProductRepository products) {
        return args -> {
            if (products.count() > 0) {
                return;
            }

            products.saveAll(List.of(
                    new Product("Mechanical Keyboard", "Accessories", new BigDecimal("89.99"), 15),
                    new Product("USB-C Dock", "Office", new BigDecimal("129.99"), 8),
                    new Product("Wireless Mouse", "Accessories", new BigDecimal("39.99"), 25)
            ));
        };
    }
}
