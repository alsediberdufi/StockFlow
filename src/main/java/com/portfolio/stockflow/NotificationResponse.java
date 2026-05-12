package com.portfolio.stockflow;

import java.time.LocalDateTime;

public record NotificationResponse(
        Long orderId,
        String message,
        LocalDateTime createdAt
) {

    public static NotificationResponse from(Notification notification) {
        return new NotificationResponse(
                notification.getOrderId(),
                notification.getMessage(),
                notification.getCreatedAt()
        );
    }
}
