package com.reliability.ecommerce.controller;

import com.reliability.ecommerce.dto.ProductRequest;
import com.reliability.ecommerce.dto.ProductResponse;
import com.reliability.ecommerce.service.ProductService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // =========================================================
    // Create Product
    // POST /api/products
    // SELLER token required
    // =========================================================

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            @Valid @RequestBody ProductRequest request) {

        ProductResponse response =
                productService.createProduct(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // =========================================================
    // Get All Products
    // GET /api/products
    // Public
    // =========================================================

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {

        return ResponseEntity.ok(
                productService.getAllProducts()
        );
    }

    // =========================================================
    // Get Product
    // GET /api/products/{id}
    // Public
    // =========================================================

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                productService.getProduct(id)
        );
    }
}

