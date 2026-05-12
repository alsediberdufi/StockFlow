package com.portfolio.stockflow;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;
    private BigDecimal amount;
    private boolean successful;
    private LocalDateTime createdAt;

    protected Payment() {
    }

    public Payment(Long orderId, BigDecimal amount, boolean successful) {
        this.orderId = orderId;
        this.amount = amount;
        this.successful = successful;
        this.createdAt = LocalDateTime.now();
    }
}
