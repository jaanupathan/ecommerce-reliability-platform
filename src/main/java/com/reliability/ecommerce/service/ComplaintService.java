package com.reliability.ecommerce.service;

import com.reliability.ecommerce.dto.ComplaintRequest;
import com.reliability.ecommerce.dto.ComplaintResponse;
import com.reliability.ecommerce.entity.Complaint;
import com.reliability.ecommerce.entity.ComplaintCategory;
import com.reliability.ecommerce.entity.ComplaintStatus;
import com.reliability.ecommerce.entity.Order;
import com.reliability.ecommerce.entity.Shipment;
import com.reliability.ecommerce.entity.User;
import com.reliability.ecommerce.event.ComplaintCreatedEvent;
import com.reliability.ecommerce.event.KafkaProducerService;
import com.reliability.ecommerce.exception.ResourceNotFoundException;
import com.reliability.ecommerce.repository.ComplaintRepository;
import com.reliability.ecommerce.repository.OrderRepository;
import com.reliability.ecommerce.repository.ShipmentRepository;
import com.reliability.ecommerce.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ComplaintService {

    private final SellerReliabilityService sellerReliabilityService;
    private final KafkaProducerService kafkaProducerService;
    private final ComplaintRepository complaintRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ShipmentRepository shipmentRepository;

    public ComplaintService(
            ComplaintRepository complaintRepository,
            UserRepository userRepository,
            OrderRepository orderRepository,
            ShipmentRepository shipmentRepository,
            KafkaProducerService kafkaProducerService,
            SellerReliabilityService sellerReliabilityService) {

        this.complaintRepository = complaintRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.shipmentRepository = shipmentRepository;
        this.kafkaProducerService = kafkaProducerService;
        this.sellerReliabilityService = sellerReliabilityService;
    }

    @Transactional
    public ComplaintResponse updateComplaintStatus(
            Long complaintId,
            String status) {

        Complaint complaint = complaintRepository
                .findById(complaintId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Complaint not found"));

        ComplaintStatus newStatus;

        try {
            newStatus = ComplaintStatus.valueOf(
                    status.toUpperCase()
            );
        } catch (IllegalArgumentException ex) {
            throw new RuntimeException(
                    "Invalid complaint status");
        }

        complaint.setStatus(newStatus);

        if (newStatus == ComplaintStatus.RESOLVED) {
            complaint.setResolvedAt(
                    java.time.LocalDateTime.now()
            );
        }

        Complaint savedComplaint =
                complaintRepository.save(complaint);

        // Clear seller reliability cache
        if (savedComplaint.getOrder() != null
                && !savedComplaint.getOrder().getItems().isEmpty()) {

            Long sellerId = savedComplaint
                    .getOrder()
                    .getItems()
                    .get(0)
                    .getProduct()
                    .getSeller()
                    .getId();

            sellerReliabilityService
                    .clearReliabilityCache(sellerId);
        }

        return mapToResponse(savedComplaint);
    }

    @Transactional
    public ComplaintResponse createComplaint(
            ComplaintRequest request) {

        User customer = userRepository
                .findById(request.getCustomerId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Customer not found"));

        Order order = orderRepository
                .findById(request.getOrderId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order not found"));

        Shipment shipment = shipmentRepository
                .findById(request.getShipmentId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Shipment not found"));

        ComplaintCategory category;

        try {
            category = ComplaintCategory.valueOf(
                    request.getCategory().toUpperCase()
            );
        } catch (Exception ex) {
            throw new RuntimeException(
                    "Invalid complaint category");
        }

        Complaint complaint = new Complaint(
                customer,
                order,
                shipment,
                category,
                request.getDescription()
        );

        Complaint savedComplaint =
                complaintRepository.save(complaint);

        // Send Kafka event
        kafkaProducerService.sendComplaintCreatedEvent(
                new ComplaintCreatedEvent(
                        savedComplaint.getId(),
                        savedComplaint.getCustomer().getId(),
                        savedComplaint.getOrder().getId(),
                        savedComplaint.getShipment().getId(),
                        savedComplaint.getCategory().name(),
                        savedComplaint.getDescription()
                )
        );

        // Clear seller reliability cache
        if (savedComplaint.getOrder() != null
                && !savedComplaint.getOrder().getItems().isEmpty()) {

            Long sellerId = savedComplaint
                    .getOrder()
                    .getItems()
                    .get(0)
                    .getProduct()
                    .getSeller()
                    .getId();

            sellerReliabilityService
                    .clearReliabilityCache(sellerId);
        }

        return mapToResponse(savedComplaint);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ComplaintResponse createDeliveryComplaint(
            Long customerId,
            Long orderId,
            Long shipmentId,
            String description) {

        ComplaintRequest request = new ComplaintRequest();

        request.setCustomerId(customerId);
        request.setOrderId(orderId);
        request.setShipmentId(shipmentId);
        request.setCategory("DELIVERY");
        request.setDescription(description);

        return createComplaint(request);
    }

    public ComplaintResponse getComplaint(Long id) {

        Complaint complaint = complaintRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Complaint not found"));

        return mapToResponse(complaint);
    }

    public java.util.List<ComplaintResponse> getComplaintsByStatus(
            String status) {

        ComplaintStatus complaintStatus;

        try {
            complaintStatus = ComplaintStatus.valueOf(
                    status.toUpperCase()
            );
        } catch (IllegalArgumentException ex) {
            throw new RuntimeException(
                    "Invalid complaint status");
        }

        return complaintRepository
                .findByStatus(complaintStatus)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private ComplaintResponse mapToResponse(
            Complaint complaint) {

        Long orderId = null;
        Long shipmentId = null;

        if (complaint.getOrder() != null) {
            orderId = complaint.getOrder().getId();
        }

        if (complaint.getShipment() != null) {
            shipmentId = complaint.getShipment().getId();
        }

        return new ComplaintResponse(
                complaint.getId(),
                complaint.getCustomer().getId(),
                orderId,
                shipmentId,
                complaint.getCategory().name(),
                complaint.getStatus().name(),
                complaint.getDescription(),
                complaint.getCreatedAt()
        );
    }
}