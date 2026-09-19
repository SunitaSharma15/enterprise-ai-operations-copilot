package com.sunita.ai.part1;

import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@Service
class OrderStatusService {
    private static final String ORDER_ID_PATTERN = "ORD-\\d{4,10}";

    private final Map<String, String> statuses = Map.of(
            "ORD-1001", "SHIPPED",
            "ORD-1002", "PROCESSING",
            "ORD-1003", "DELIVERED");

    Optional<OrderStatus> findStatus(String orderId) {
        String normalizedId = normalizeAndValidate(orderId);
        return Optional.ofNullable(statuses.get(normalizedId))
                .map(status -> new OrderStatus(normalizedId, status));
    }

    String normalizeAndValidate(String orderId) {
        if (orderId == null || orderId.isBlank()) {
            throw new IllegalArgumentException("Order ID is required");
        }

        String normalizedId = orderId.trim().toUpperCase(Locale.ROOT);
        if (!normalizedId.matches(ORDER_ID_PATTERN)) {
            throw new IllegalArgumentException("Order ID must match ORD- followed by 4 to 10 digits");
        }
        return normalizedId;
    }
}

record OrderStatus(String orderId, String status) {}
