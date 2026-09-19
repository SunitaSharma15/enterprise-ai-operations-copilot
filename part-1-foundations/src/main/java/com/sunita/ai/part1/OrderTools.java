package com.sunita.ai.part1;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Component
class OrderTools {
    private final OrderStatusService orderStatusService;

    OrderTools(OrderStatusService orderStatusService) {
        this.orderStatusService = orderStatusService;
    }

    @Tool(description = "Get order status by order ID")
    OrderStatus getOrderStatus(@ToolParam(description = "Order ID such as ORD-1001") String orderId) {
        return orderStatusService.findStatus(orderId)
                .orElseGet(() -> new OrderStatus(orderStatusService.normalizeAndValidate(orderId),
                		"NOT_FOUND"));
    }
}
