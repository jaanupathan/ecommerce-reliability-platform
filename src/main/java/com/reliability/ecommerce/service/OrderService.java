package com.reliability.ecommerce.service;

import com.reliability.ecommerce.dto.OrderItemRequest;
import com.reliability.ecommerce.dto.OrderItemResponse;
import com.reliability.ecommerce.dto.OrderRequest;
import com.reliability.ecommerce.dto.OrderResponse;
import com.reliability.ecommerce.entity.Inventory;
import com.reliability.ecommerce.entity.Order;
import com.reliability.ecommerce.entity.OrderItem;
import com.reliability.ecommerce.entity.OrderStatus;
import com.reliability.ecommerce.entity.Product;
import com.reliability.ecommerce.entity.Role;
import com.reliability.ecommerce.entity.User;
import com.reliability.ecommerce.event.KafkaProducerService;
import com.reliability.ecommerce.event.OrderCancelledEvent;
import com.reliability.ecommerce.event.OrderCreatedEvent;
import com.reliability.ecommerce.exception.ResourceNotFoundException;
import com.reliability.ecommerce.repository.InventoryRepository;
import com.reliability.ecommerce.repository.OrderRepository;
import com.reliability.ecommerce.repository.ProductRepository;
import com.reliability.ecommerce.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    private final KafkaProducerService kafkaProducerService;
    private final SellerReliabilityService sellerReliabilityService;

    public OrderService(
            OrderRepository orderRepository,
            UserRepository userRepository,
            ProductRepository productRepository,
            InventoryRepository inventoryRepository,
            KafkaProducerService kafkaProducerService,
            SellerReliabilityService sellerReliabilityService) {

        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
        this.kafkaProducerService = kafkaProducerService;
        this.sellerReliabilityService = sellerReliabilityService;
    }

    @Transactional
    public OrderResponse createOrder(OrderRequest request) {

        User customer = userRepository
                .findById(request.getCustomerId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Customer not found"));

        if (customer.getRole() != Role.CUSTOMER) {
            throw new RuntimeException(
                    "Only CUSTOMER can create orders");
        }

        if (request.getItems() == null
                || request.getItems().isEmpty()) {

            throw new IllegalArgumentException(
                    "Order must contain at least one item");
        }

        Order order = new Order(customer, 0.0);

        double totalAmount = 0.0;

        for (OrderItemRequest itemRequest : request.getItems()) {

            if (itemRequest.getProductId() == null) {
                throw new IllegalArgumentException(
                        "Product ID is required");
            }

            if (itemRequest.getQuantity() == null
                    || itemRequest.getQuantity() <= 0) {

                throw new IllegalArgumentException(
                        "Quantity must be greater than zero");
            }

            Product product = productRepository
                    .findById(itemRequest.getProductId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Product not found"));

            Inventory inventory = inventoryRepository
                    .findByProductId(product.getId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Inventory not found for product: "
                                            + product.getName()));

            if (inventory.getAvailableQuantity()
                    < itemRequest.getQuantity()) {

                throw new RuntimeException(
                        "Insufficient inventory for product: "
                                + product.getName());
            }

            inventory.setAvailableQuantity(
                    inventory.getAvailableQuantity()
                            - itemRequest.getQuantity()
            );

            inventory.setReservedQuantity(
                    inventory.getReservedQuantity()
                            + itemRequest.getQuantity()
            );

            inventoryRepository.save(inventory);

            double itemTotal =
                    product.getPrice()
                            * itemRequest.getQuantity();

            OrderItem orderItem = new OrderItem(
                    product,
                    itemRequest.getQuantity(),
                    product.getPrice()
            );

            order.addItem(orderItem);

            totalAmount += itemTotal;
        }

        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);

        // Publish Order Created event
        kafkaProducerService.sendOrderCreatedEvent(
                new OrderCreatedEvent(
                        savedOrder.getId(),
                        savedOrder.getCustomer().getId(),
                        savedOrder.getTotalAmount()
                )
        );

        return mapToResponse(savedOrder);
    }

    @Transactional
    public OrderResponse cancelOrder(Long orderId) {

        Order order = orderRepository
                .findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order not found"));

        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new RuntimeException(
                    "Order is already cancelled");
        }

        if (order.getStatus() == OrderStatus.DELIVERED) {
            throw new RuntimeException(
                    "Delivered order cannot be cancelled");
        }

        // Restore inventory
        for (OrderItem item : order.getItems()) {

            Inventory inventory = inventoryRepository
                    .findByProductId(
                            item.getProduct().getId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Inventory not found for product: "
                                            + item.getProduct().getName()));

            inventory.setAvailableQuantity(
                    inventory.getAvailableQuantity()
                            + item.getQuantity()
            );

            inventory.setReservedQuantity(
                    inventory.getReservedQuantity()
                            - item.getQuantity()
            );

            inventoryRepository.save(inventory);
        }

        order.setStatus(OrderStatus.CANCELLED);

        Order cancelledOrder =
                orderRepository.save(order);

        // Clear seller reliability cache
        for (OrderItem item : cancelledOrder.getItems()) {

            Long sellerId = item
                    .getProduct()
                    .getSeller()
                    .getId();

            sellerReliabilityService
                    .clearReliabilityCache(sellerId);
        }

        // Publish Order Cancelled event
        kafkaProducerService.sendOrderCancelledEvent(
                new OrderCancelledEvent(
                        cancelledOrder.getId(),
                        cancelledOrder.getCustomer().getId(),
                        "Customer cancelled the order"
                )
        );

        return mapToResponse(cancelledOrder);
    }

    public OrderResponse getOrder(Long id) {

        Order order = orderRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order not found"));

        return mapToResponse(order);
    }

    private OrderResponse mapToResponse(Order order) {

        List<OrderItemResponse> itemResponses =
                new ArrayList<>();

        for (OrderItem item : order.getItems()) {

            itemResponses.add(
                    new OrderItemResponse(
                            item.getProduct().getId(),
                            item.getProduct().getName(),
                            item.getQuantity(),
                            item.getPrice()
                    )
            );
        }

        return new OrderResponse(
                order.getId(),
                order.getCustomer().getId(),
                order.getStatus().name(),
                order.getTotalAmount(),
                order.getCreatedAt(),
                itemResponses
        );
    }
}