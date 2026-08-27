package com.reliability.ecommerce.dto;

public class OrderItemResponse {

    private Long productId;
    private String productName;
    private Integer quantity;
    private Double price;

    public OrderItemResponse(
            Long productId,
            String productName,
            Integer quantity,
            Double price) {

        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Double getPrice() {
        return price;
    }
}