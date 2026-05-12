package com.portfolio.stockflow;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record InvoiceResponse(
        Long orderId,
        String invoiceNumber,
        BigDecimal total,
        LocalDateTime createdAt
) {

    public static InvoiceResponse from(Invoice invoice) {
        return new InvoiceResponse(
                invoice.getOrderId(),
                invoice.getInvoiceNumber(),
                invoice.getTotal(),
                invoice.getCreatedAt()
        );
    }
}
