package com.portfolio.stockflow;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orders;

    public OrderController(OrderService orders) {
        this.orders = orders;
    }

    @PostMapping
    public OrderResponse create(@Valid @RequestBody CreateOrderRequest request) {
        return orders.create(request);
    }

    @GetMapping
    public List<OrderResponse> findAll(
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(required = false) String customerName
    ) {
        return orders.findAll(status, customerName);
    }

    @GetMapping("/{id}")
    public OrderResponse findById(@PathVariable Long id) {
        return orders.findResponseById(id);
    }

    @PostMapping("/{id}/pay")
    public OrderResponse markPaid(@PathVariable Long id) {
        return orders.markPaid(id);
    }

    @PostMapping("/{id}/invoice")
    public OrderResponse createInvoice(@PathVariable Long id) {
        return orders.createInvoice(id);
    }

    @PostMapping("/{id}/cancel")
    public OrderResponse cancel(@PathVariable Long id) {
        return orders.cancel(id);
    }
}
