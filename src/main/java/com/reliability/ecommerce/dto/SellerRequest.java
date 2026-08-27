package com.reliability.ecommerce.dto;

public class SellerRequest {

    private Long userId;
    private String businessName;

    public SellerRequest() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }
}