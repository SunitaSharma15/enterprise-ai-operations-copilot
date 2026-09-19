package com.sunita.ai.part1;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class OrderToolsTest {
    private final OrderTools tools = new OrderTools(new OrderStatusService());

    @Test
    void returnsKnownStatus() {
        assertThat(tools.getOrderStatus("ORD-1001").status()).isEqualTo("SHIPPED");
    }

    @Test
    void rejectsMalformedId() {
        assertThatThrownBy(() -> tools.getOrderStatus("DROP TABLE"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void returnsNotFoundWithoutInventingAStatus() {
        assertThat(tools.getOrderStatus("ORD-9999").status()).isEqualTo("NOT_FOUND");
    }
}
