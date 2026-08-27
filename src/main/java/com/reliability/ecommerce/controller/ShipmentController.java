package com.reliability.ecommerce.controller;

import com.reliability.ecommerce.dto.OtpVerificationRequest;
import com.reliability.ecommerce.dto.ShipmentResponse;
import com.reliability.ecommerce.service.ShipmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shipments")
public class ShipmentController {

    private final ShipmentService shipmentService;

    public ShipmentController(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    @PostMapping("/order/{orderId}")
    public ResponseEntity<ShipmentResponse> createShipment(
            @PathVariable Long orderId) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(shipmentService.createShipment(orderId));
    }

    @PutMapping("/{shipmentId}/start")
    public ResponseEntity<ShipmentResponse> startDelivery(
            @PathVariable Long shipmentId) {

        return ResponseEntity.ok(
                shipmentService.startDelivery(shipmentId)
        );
    }

    @PostMapping("/{shipmentId}/otp")
    public ResponseEntity<ShipmentResponse> generateOtp(
            @PathVariable Long shipmentId) {

        return ResponseEntity.ok(
                shipmentService.generateOtp(shipmentId)
        );
    }

    @PostMapping("/{shipmentId}/verify-otp")
    public ResponseEntity<ShipmentResponse> verifyOtp(
            @PathVariable Long shipmentId,
            @RequestBody OtpVerificationRequest request) {

        return ResponseEntity.ok(
                shipmentService.verifyOtp(
                        shipmentId,
                        request
                )
        );
    }
}