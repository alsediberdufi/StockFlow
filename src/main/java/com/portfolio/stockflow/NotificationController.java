package com.portfolio.stockflow;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationRepository notifications;

    public NotificationController(NotificationRepository notifications) {
        this.notifications = notifications;
    }

    @GetMapping("/order/{orderId}")
    public List<NotificationResponse> findByOrderId(@PathVariable Long orderId) {
        return notifications.findByOrderId(orderId)
                .stream()
                .map(NotificationResponse::from)
                .toList();
    }
}
