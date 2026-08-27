
package com.reliability.ecommerce.service;

import com.reliability.ecommerce.dto.ComplaintResponse;
import com.reliability.ecommerce.entity.Complaint;
import com.reliability.ecommerce.entity.ComplaintCategory;
import com.reliability.ecommerce.entity.Order;
import com.reliability.ecommerce.entity.Role;
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
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeliveryComplaintService {

    private final ComplaintRepository complaintRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ShipmentRepository shipmentRepository;
    private final KafkaProducerService kafkaProducerService;

    public DeliveryComplaintService(
            ComplaintRepository complaintRepository,
            UserRepository userRepository,
            OrderRepository orderRepository,
            ShipmentRepository shipmentRepository,
            KafkaProducerService kafkaProducerService) {

        this.complaintRepository = complaintRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.shipmentRepository = shipmentRepository;
        this.kafkaProducerService = kafkaProducerService;
    }

    @Transactional
    public ComplaintResponse createDeliveryComplaint(
            Long customerId,
            Long orderId,
            Long shipmentId,
            String description) {

        // 1. Find customer
        User customer = userRepository
                .findById(customerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Customer not found"));

        // 2. Check customer role
        if (customer.getRole() != Role.CUSTOMER) {
            throw new RuntimeException(
                    "Only CUSTOMER can create delivery complaints");
        }

        // 3. Find order
        Order order = orderRepository
                .findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order not found"));

        // 4. Find shipment
        Shipment shipment = shipmentRepository
                .findById(shipmentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Shipment not found"));

        // 5. Validate description
        if (description == null ||
                description.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Complaint description is required");
        }

        // 6. Create complaint
        Complaint complaint = new Complaint(
                customer,
                order,
                shipment,
                ComplaintCategory.DELIVERY,
                description
        );

        // 7. Save complaint
        Complaint savedComplaint =
                complaintRepository.save(complaint);

        // 8. Publish Kafka event
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

        // 9. Return response
        return new ComplaintResponse(
                savedComplaint.getId(),
                savedComplaint.getCustomer().getId(),
                savedComplaint.getOrder().getId(),
                savedComplaint.getShipment().getId(),
                savedComplaint.getCategory().name(),
                savedComplaint.getStatus().name(),
                savedComplaint.getDescription(),
                savedComplaint.getCreatedAt()
        );
    }
}

