package com.reliability.ecommerce.service;

import com.reliability.ecommerce.dto.InventoryRequest;
import com.reliability.ecommerce.dto.InventoryResponse;
import com.reliability.ecommerce.entity.Inventory;
import com.reliability.ecommerce.entity.Product;
import com.reliability.ecommerce.exception.ResourceNotFoundException;
import com.reliability.ecommerce.repository.InventoryRepository;
import com.reliability.ecommerce.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;

    public InventoryService(
            InventoryRepository inventoryRepository,
            ProductRepository productRepository) {

        this.inventoryRepository = inventoryRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public InventoryResponse createInventory(InventoryRequest request) {

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found"));

        if (request.getAvailableQuantity() == null ||
                request.getAvailableQuantity() < 0) {

            throw new IllegalArgumentException(
                    "Available quantity cannot be negative");
        }

        if (inventoryRepository.findByProductId(product.getId()).isPresent()) {
            throw new IllegalArgumentException(
                    "Inventory already exists for this product");
        }

        Inventory inventory = new Inventory(
                product,
                request.getAvailableQuantity()
        );

        Inventory savedInventory = inventoryRepository.save(inventory);

        return mapToResponse(savedInventory);
    }

    public InventoryResponse getInventory(Long productId) {

        Inventory inventory = inventoryRepository
                .findByProductId(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Inventory not found for product"));

        return mapToResponse(inventory);
    }

    private InventoryResponse mapToResponse(Inventory inventory) {

        int available = inventory.getAvailableQuantity();
        int reserved = inventory.getReservedQuantity();

        return new InventoryResponse(
                inventory.getId(),
                inventory.getProduct().getId(),
                inventory.getProduct().getName(),
                available,
                reserved,
                available + reserved
        );
    }
}