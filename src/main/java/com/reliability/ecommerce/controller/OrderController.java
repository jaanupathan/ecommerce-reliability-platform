package com.reliability.ecommerce.controller;

import com.reliability.ecommerce.dto.OrderRequest;
import com.reliability.ecommerce.dto.OrderResponse;
import com.reliability.ecommerce.service.OrderService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // =========================================================
    // Create Order
    // POST /api/orders
    // CUSTOMER token required
    // =========================================================

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @Valid @RequestBody OrderRequest request) {

        OrderResponse response =
                orderService.createOrder(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // =========================================================
    // Cancel Order
    // PUT /api/orders/{id}/cancel
    // CUSTOMER token required
    // =========================================================

    @PutMapping("/{id}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                orderService.cancelOrder(id)
        );
    }

    // =========================================================
    // Get Order
    // GET /api/orders/{id}
    // Authentication required
    // =========================================================

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrder(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                orderService.getOrder(id)
        );
    }
}

