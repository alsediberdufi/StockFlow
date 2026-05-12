package com.portfolio.stockflow;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface OrderRepository extends JpaRepository<CustomerOrder, Long> {

    long countByStatus(OrderStatus status);

    List<CustomerOrder> findByStatus(OrderStatus status);

    List<CustomerOrder> findByCustomerNameContainingIgnoreCase(String customerName);

    List<CustomerOrder> findByStatusAndCustomerNameContainingIgnoreCase(OrderStatus status, String customerName);

    @Query("select coalesce(sum(o.total), 0) from CustomerOrder o where o.status in ('PAID', 'INVOICED')")
    BigDecimal calculateRevenue();

    @Query("select coalesce(avg(o.total), 0) from CustomerOrder o where o.status in ('PAID', 'INVOICED')")
    Double calculateAverageOrderValue();
}
