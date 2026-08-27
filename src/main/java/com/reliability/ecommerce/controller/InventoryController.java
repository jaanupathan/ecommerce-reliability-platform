package com.reliability.ecommerce.controller;

import com.reliability.ecommerce.dto.InventoryRequest;
import com.reliability.ecommerce.dto.InventoryResponse;
import com.reliability.ecommerce.service.InventoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping
    public ResponseEntity<InventoryResponse> createInventory(
            @RequestBody InventoryRequest request) {

        InventoryResponse response =
                inventoryService.createInventory(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<InventoryResponse> getInventory(
            @PathVariable Long productId) {

        return ResponseEntity.ok(
                inventoryService.getInventory(productId)
        );
    }
}