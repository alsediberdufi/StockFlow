package com.portfolio.stockflow;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("""
            select i.productName as productName, sum(i.quantity) as quantity
            from OrderItem i
            where i.order.status in ('PAID', 'INVOICED')
            group by i.productName
            order by sum(i.quantity) desc
            """)
    List<BestSellingProductRow> findBestSellingProducts(Pageable pageable);

    interface BestSellingProductRow {
        String getProductName();

        Long getQuantity();
    }
}
