package com.reliability.ecommerce.dto;

public class SellerReliabilityResponse {

    private Long sellerId;
    private String businessName;
    private Integer totalOrders;
    private Integer cancelledOrders;
    private Integer complaintCount;
    private Double cancellationRate;
    private Double returnRate;
    private Double complaintRate;
    private Double trustScore;

    public SellerReliabilityResponse(
            Long sellerId,
            String businessName,
            Integer totalOrders,
            Integer cancelledOrders,
            Integer complaintCount,
            Double cancellationRate,
            Double returnRate,
            Double complaintRate,
            Double trustScore) {

        this.sellerId = sellerId;
        this.businessName = businessName;
        this.totalOrders = totalOrders;
        this.cancelledOrders = cancelledOrders;
        this.complaintCount = complaintCount;
        this.cancellationRate = cancellationRate;
        this.returnRate = returnRate;
        this.complaintRate = complaintRate;
        this.trustScore = trustScore;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public String getBusinessName() {
        return businessName;
    }

    public Integer getTotalOrders() {
        return totalOrders;
    }

    public Integer getCancelledOrders() {
        return cancelledOrders;
    }

    public Integer getComplaintCount() {
        return complaintCount;
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

    public Double getTrustScore() {
        return trustScore;
    }
}