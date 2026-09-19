package com.sunita.ai.part1;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderStatusServiceTest {
    private final OrderStatusService service = new OrderStatusService();

    @Test
    void normalizesAndReturnsKnownOrder() {
        OrderStatus result = service.findStatus(" ord-1001 ").orElseThrow();

        assertThat(result.orderId()).isEqualTo("ORD-1001");
        assertThat(result.status()).isEqualTo("SHIPPED");
    }

    @Test
    void returnsEmptyForUnknownValidOrder() {
        assertThat(service.findStatus("ORD-9999")).isEmpty();
    }

    @Test
    void rejectsMalformedOrderId() {
        assertThatThrownBy(() -> service.findStatus("1001"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ORD-");
    }
}
