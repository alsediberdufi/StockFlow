package com.portfolio.stockflow;

import java.math.BigDecimal;

public record DashboardResponse(
        long reservedOrders,
        long paidOrders,
        long invoicedOrders,
        long cancelledOrders,
        BigDecimal revenue,
        long totalProducts,
        long lowStockProducts,
        long totalOrders,
        BigDecimal averageOrderValue,
        BestSellingProductResponse bestSellingProduct
) {
}
