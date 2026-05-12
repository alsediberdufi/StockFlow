package com.portfolio.stockflow;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    private final InvoiceRepository invoices;

    public InvoiceController(InvoiceRepository invoices) {
        this.invoices = invoices;
    }

    @GetMapping("/order/{orderId}")
    public List<InvoiceResponse> findByOrderId(@PathVariable Long orderId) {
        return invoices.findByOrderId(orderId)
                .stream()
                .map(InvoiceResponse::from)
                .toList();
    }
}
