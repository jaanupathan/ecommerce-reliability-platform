package com.reliability.ecommerce.dto;

public class SellerResponse {

    private Long id;
    private Long userId;
    private String businessName;
    private Double trustScore;
    private Double cancellationRate;
    private Double returnRate;
    private Double complaintRate;

    public SellerResponse() {
    }

    public SellerResponse(
            Long id,
            Long userId,
            String businessName,
            Double trustScore,
            Double cancellationRate,
            Double returnRate,
            Double complaintRate) {

        this.id = id;
        this.userId = userId;
        this.businessName = businessName;
        this.trustScore = trustScore;
        this.cancellationRate = cancellationRate;
        this.returnRate = returnRate;
        this.complaintRate = complaintRate;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getBusinessName() {
        return businessName;
    }

    public Double getTrustScore() {
        return trustScore;
    }

    public Double getCancellationRate() {
        return cancellationRate;
    }

    public Double getReturnRate() {
        return returnRate;
    }

    public Double getComplaintRate() {
        return complaintRate;
    }
}