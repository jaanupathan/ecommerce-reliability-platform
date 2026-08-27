package com.reliability.ecommerce.event;

public class OrderCancelledEvent {

    private Long orderId;
    private Long customerId;
    private String reason;

    public OrderCancelledEvent() {
    }

    public OrderCancelledEvent(
            Long orderId,
            Long customerId,
            String reason) {

        this.orderId = orderId;
        this.customerId = customerId;
        this.reason = reason;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public String getReason() {
        return reason;
    }
}