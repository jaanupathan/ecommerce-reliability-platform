package com.reliability.ecommerce.event;

public class OrderCreatedEvent {

    private Long orderId;
    private Long customerId;
    private Double totalAmount;

    public OrderCreatedEvent() {
    }

    public OrderCreatedEvent(
            Long orderId,
            Long customerId,
            Double totalAmount) {

        this.orderId = orderId;
        this.customerId = customerId;
        this.totalAmount = totalAmount;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }
}