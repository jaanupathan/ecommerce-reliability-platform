package com.reliability.ecommerce.dto;

public class InventoryResponse {

    private Long id;
    private Long productId;
    private String productName;
    private Integer availableQuantity;
    private Integer reservedQuantity;
    private Integer totalQuantity;

    public InventoryResponse() {
    }

    public InventoryResponse(
            Long id,
            Long productId,
            String productName,
            Integer availableQuantity,
            Integer reservedQuantity,
            Integer totalQuantity) {

        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.availableQuantity = availableQuantity;
        this.reservedQuantity = reservedQuantity;
        this.totalQuantity = totalQuantity;
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public Integer getAvailableQuantity() {
        return availableQuantity;
    }

    public Integer getReservedQuantity() {
        return reservedQuantity;
    }

    public Integer getTotalQuantity() {
        return totalQuantity;
    }
}