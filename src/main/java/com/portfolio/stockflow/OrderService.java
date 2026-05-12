package com.portfolio.stockflow;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orders;
    private final ProductService products;
    private final PaymentRepository payments;
    private final InvoiceRepository invoices;
    private final NotificationRepository notifications;

    public OrderService(
            OrderRepository orders,
            ProductService products,
            PaymentRepository payments,
            InvoiceRepository invoices,
            NotificationRepository notifications
    ) {
        this.orders = orders;
        this.products = products;
        this.payments = payments;
        this.invoices = invoices;
        this.notifications = notifications;
    }

    @Transactional
    public OrderResponse create(CreateOrderRequest request) {
        CustomerOrder order = new CustomerOrder(request.customerName());

        for (CreateOrderItemRequest itemRequest : request.items()) {
            Product product = products.findById(itemRequest.productId());
            product.reserveStock(itemRequest.quantity());

            order.addItem(new OrderItem(
                    product.getId(),
                    product.getName(),
                    itemRequest.quantity(),
                    product.getPrice()
            ));
        }

        return OrderResponse.from(orders.save(order));
    }

    public List<OrderResponse> findAll(OrderStatus status, String customerName) {
        List<CustomerOrder> result;

        if (status != null && hasText(customerName)) {
            result = orders.findByStatusAndCustomerNameContainingIgnoreCase(status, customerName);
        } else if (status != null) {
            result = orders.findByStatus(status);
        } else if (hasText(customerName)) {
            result = orders.findByCustomerNameContainingIgnoreCase(customerName);
        } else {
            result = orders.findAll();
        }

        return result.stream()
                .map(OrderResponse::from)
                .toList();
    }

    public OrderResponse findResponseById(Long id) {
        return OrderResponse.from(findById(id));
    }

    public CustomerOrder findById(Long id) {
        return orders.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found: " + id));
    }

    @Transactional
    public OrderResponse markPaid(Long id) {
        CustomerOrder order = findById(id);

        if (order.getStatus() != OrderStatus.RESERVED) {
            throw new BusinessException("Only reserved orders can be paid");
        }

        payments.save(new Payment(order.getId(), order.getTotal(), true));
        order.markPaid();
        return OrderResponse.from(order);
    }

    @Transactional
    public OrderResponse createInvoice(Long id) {
        CustomerOrder order = findById(id);

        if (order.getStatus() != OrderStatus.PAID) {
            throw new BusinessException("Only paid orders can be invoiced");
        }

        invoices.save(new Invoice(order.getId(), "INV-" + order.getId(), order.getTotal()));
        notifications.save(new Notification(order.getId(), "Order " + order.getId() + " was invoiced."));
        order.markInvoiced();
        return OrderResponse.from(order);
    }

    @Transactional
    public OrderResponse cancel(Long id) {
        CustomerOrder order = findById(id);

        if (order.getStatus() != OrderStatus.RESERVED) {
            throw new BusinessException("Only reserved orders can be cancelled");
        }

        for (OrderItem item : order.getItems()) {
            Product product = products.findById(item.getProductId());
            product.returnStock(item.getQuantity());
        }

        order.markCancelled();
        return OrderResponse.from(order);
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
