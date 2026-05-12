package com.portfolio.stockflow;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;
    private String invoiceNumber;
    private BigDecimal total;
    private LocalDateTime createdAt;

    protected Invoice() {
    }

    public Invoice(Long orderId, String invoiceNumber, BigDecimal total) {
        this.orderId = orderId;
        this.invoiceNumber = invoiceNumber;
        this.total = total;
        this.createdAt = LocalDateTime.now();
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
