package com.example.event;

import lombok.Getter;
import java.math.BigDecimal;

@Getter
public class OrderCreatedEvent {
    private final Object source;
    private final Long orderId;
    private final String orderNumber;
    private final Long userId;
    private final BigDecimal totalAmount;

    public OrderCreatedEvent(Object source, Long orderId, String orderNumber, Long userId, BigDecimal totalAmount) {
        this.source = source;
        this.orderId = orderId;
        this.orderNumber = orderNumber;
        this.userId = userId;
        this.totalAmount = totalAmount;
    }
}
