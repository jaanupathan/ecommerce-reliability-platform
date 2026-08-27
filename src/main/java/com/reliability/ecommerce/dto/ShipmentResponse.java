package com.reliability.ecommerce.dto;

public class ShipmentResponse {

    private Long id;
    private Long orderId;
    private String status;
    private String otp;
    private Integer otpAttempts;

    public ShipmentResponse() {
    }

    public ShipmentResponse(
            Long id,
            Long orderId,
            String status,
            String otp,
            Integer otpAttempts) {

        this.id = id;
        this.orderId = orderId;
        this.status = status;
        this.otp = otp;
        this.otpAttempts = otpAttempts;
    }

    public Long getId() {
        return id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getStatus() {
        return status;
    }

    public String getOtp() {
        return otp;
    }

    public Integer getOtpAttempts() {
        return otpAttempts;
    }
}