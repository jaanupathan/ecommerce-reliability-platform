package com.reliability.ecommerce.dto;

public class ProductResponse {

    private Long id;
    private String name;
    private String description;
    private Double price;
    private String category;
    private String brand;
    private Long sellerId;
    private String businessName;
    private Boolean active;

    public ProductResponse() {
    }

    public ProductResponse(
            Long id,
            String name,
            String description,
            Double price,
            String category,
            String brand,
            Long sellerId,
            String businessName,
            Boolean active) {

        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.brand = brand;
        this.sellerId = sellerId;
        this.businessName = businessName;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Double getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public String getBrand() {
        return brand;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public String getBusinessName() {
        return businessName;
    }

    public Boolean getActive() {
        return active;
    }
}