package com.reliability.ecommerce.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "sellers")
public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false)
    private String businessName;

    @Column(nullable = false)
    private Double trustScore = 100.0;

    @Column(nullable = false)
    private Double cancellationRate = 0.0;

    @Column(nullable = false)
    private Double returnRate = 0.0;

    @Column(nullable = false)
    private Double complaintRate = 0.0;

    public Seller() {
    }

    public Seller(User user, String businessName) {
        this.user = user;
        this.businessName = businessName;
        this.trustScore = 100.0;
        this.cancellationRate = 0.0;
        this.returnRate = 0.0;
        this.complaintRate = 0.0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public Double getTrustScore() {
        return trustScore;
    }

    public void setTrustScore(Double trustScore) {
        this.trustScore = trustScore;
    }

    public Double getCancellationRate() {
        return cancellationRate;
    }

    public void setCancellationRate(Double cancellationRate) {
        this.cancellationRate = cancellationRate;
    }

    public Double getReturnRate() {
        return returnRate;
    }

    public void setReturnRate(Double returnRate) {
        this.returnRate = returnRate;
    }

    public Double getComplaintRate() {
        return complaintRate;
    }

    public void setComplaintRate(Double complaintRate) {
        this.complaintRate = complaintRate;
    }
}