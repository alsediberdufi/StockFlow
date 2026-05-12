package com.portfolio.stockflow;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;
    private String message;
    private LocalDateTime createdAt;

    protected Notification() {
    }

    public Notification(Long orderId, String message) {
        this.orderId = orderId;
        this.message = message;
        this.createdAt = LocalDateTime.now();
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
