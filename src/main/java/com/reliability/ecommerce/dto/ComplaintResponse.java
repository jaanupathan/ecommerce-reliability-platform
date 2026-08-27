package com.reliability.ecommerce.dto;

import java.time.LocalDateTime;

public class ComplaintResponse {

    private Long id;
    private Long customerId;
    private Long orderId;
    private Long shipmentId;
    private String category;
    private String status;
    private String description;
    private LocalDateTime createdAt;

    public ComplaintResponse(
            Long id,
            Long customerId,
            Long orderId,
            Long shipmentId,
            String category,
            String status,
            String description,
            LocalDateTime createdAt) {

        this.id = id;
        this.customerId = customerId;
        this.orderId = orderId;
        this.shipmentId = shipmentId;
        this.category = category;
        this.status = status;
        this.description = description;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getShipmentId() {
        return shipmentId;
    }

    public String getCategory() {
        return category;
    }

    public String getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}