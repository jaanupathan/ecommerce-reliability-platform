package com.reliability.ecommerce.controller;

import com.reliability.ecommerce.dto.SellerReliabilityResponse;
import com.reliability.ecommerce.service.SellerReliabilityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/seller-reliability")
public class SellerReliabilityController {

    private final SellerReliabilityService sellerReliabilityService;

    public SellerReliabilityController(
            SellerReliabilityService sellerReliabilityService) {

        this.sellerReliabilityService =
                sellerReliabilityService;
    }

    @GetMapping("/{sellerId}")
    public ResponseEntity<SellerReliabilityResponse>
    getReliability(
            @PathVariable Long sellerId) {

        return ResponseEntity.ok(
                sellerReliabilityService
                        .calculateReliability(sellerId)
        );
    }
}