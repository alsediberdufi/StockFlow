package com.portfolio.stockflow;

import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private static final int LOW_STOCK_LIMIT = 4;

    private final OrderRepository orders;
    private final ProductRepository products;
    private final OrderItemRepository orderItems;

    public AnalyticsController(
            OrderRepository orders,
            ProductRepository products,
            OrderItemRepository orderItems
    ) {
        this.orders = orders;
        this.products = products;
        this.orderItems = orderItems;
    }

    @GetMapping("/dashboard")
    public DashboardResponse getDashboard() {
        return new DashboardResponse(
                orders.countByStatus(OrderStatus.RESERVED),
                orders.countByStatus(OrderStatus.PAID),
                orders.countByStatus(OrderStatus.INVOICED),
                orders.countByStatus(OrderStatus.CANCELLED),
                orders.calculateRevenue(),
                products.count(),
                products.countByStockLessThanEqual(LOW_STOCK_LIMIT),
                orders.count(),
                averageOrderValue(),
                bestSellingProduct()
        );
    }

    private BigDecimal averageOrderValue() {
        Double average = orders.calculateAverageOrderValue();
        return BigDecimal.valueOf(average == null ? 0 : average)
                .setScale(2, RoundingMode.HALF_UP);
    }

    private BestSellingProductResponse bestSellingProduct() {
        return orderItems.findBestSellingProducts(PageRequest.of(0, 1))
                .stream()
                .findFirst()
                .map(row -> new BestSellingProductResponse(row.getProductName(), row.getQuantity()))
                .orElse(new BestSellingProductResponse("None", 0));
    }
}
