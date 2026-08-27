package com.reliability.ecommerce.event;

public class ComplaintCreatedEvent {

    private Long complaintId;
    private Long customerId;
    private Long orderId;
    private Long shipmentId;
    private String category;
    private String description;

    public ComplaintCreatedEvent() {
    }

    public ComplaintCreatedEvent(
            Long complaintId,
            Long customerId,
            Long orderId,
            Long shipmentId,
            String category,
            String description) {

        this.complaintId = complaintId;
        this.customerId = customerId;
        this.orderId = orderId;
        this.shipmentId = shipmentId;
        this.category = category;
        this.description = description;
    }

    public Long getComplaintId() {
        return complaintId;
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

    public String getDescription() {
        return description;
    }
}