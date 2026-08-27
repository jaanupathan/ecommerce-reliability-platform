package com.reliability.ecommerce.controller;

import com.reliability.ecommerce.dto.SellerRequest;
import com.reliability.ecommerce.dto.SellerResponse;
import com.reliability.ecommerce.service.SellerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sellers")
public class SellerController {

    private final SellerService sellerService;

    public SellerController(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    @PostMapping
    public ResponseEntity<SellerResponse> createSeller(
            @RequestBody SellerRequest request) {

        SellerResponse response =
                sellerService.createSeller(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SellerResponse> getSeller(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                sellerService.getSeller(id)
        );
    }
}