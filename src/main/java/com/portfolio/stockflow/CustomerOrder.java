package com.portfolio.stockflow;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class CustomerOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerName;
    private BigDecimal total;
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    protected CustomerOrder() {
    }

    public CustomerOrder(String customerName) {
        this.customerName = customerName;
        this.total = BigDecimal.ZERO;
        this.createdAt = LocalDateTime.now();
        this.status = OrderStatus.RESERVED;
    }

    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
        total = total.add(item.getLineTotal());
    }

    public void markPaid() {
        status = OrderStatus.PAID;
    }

    public void markInvoiced() {
        status = OrderStatus.INVOICED;
    }

    public void markCancelled() {
        status = OrderStatus.CANCELLED;
    }

    public void markFailed() {
        status = OrderStatus.FAILED;
    }

    public Long getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public List<OrderItem> getItems() {
        return items;
    }
}
