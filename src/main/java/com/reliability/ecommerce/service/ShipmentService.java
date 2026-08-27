package com.reliability.ecommerce.service;

import com.reliability.ecommerce.dto.OtpVerificationRequest;
import com.reliability.ecommerce.dto.ShipmentResponse;
import com.reliability.ecommerce.entity.Order;
import com.reliability.ecommerce.entity.OrderStatus;
import com.reliability.ecommerce.entity.Shipment;
import com.reliability.ecommerce.entity.ShipmentStatus;
import com.reliability.ecommerce.exception.ResourceNotFoundException;
import com.reliability.ecommerce.repository.OrderRepository;
import com.reliability.ecommerce.repository.ShipmentRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final OrderRepository orderRepository;
    private final ComplaintService complaintService;

    public ShipmentService(
            ShipmentRepository shipmentRepository,
            OrderRepository orderRepository,
            ComplaintService complaintService) {

        this.shipmentRepository = shipmentRepository;
        this.orderRepository = orderRepository;
        this.complaintService = complaintService;
    }

    @Transactional
    public ShipmentResponse createShipment(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order not found"));

        if (shipmentRepository.findByOrderId(orderId).isPresent()) {
            throw new RuntimeException(
                    "Shipment already exists for this order");
        }

        Shipment shipment = new Shipment(order);

        Shipment savedShipment = shipmentRepository.save(shipment);

        return mapToResponse(savedShipment);
    }

    @Transactional
    public ShipmentResponse startDelivery(Long shipmentId) {

        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Shipment not found"));

        shipment.setStatus(ShipmentStatus.OUT_FOR_DELIVERY);

        Order order = shipment.getOrder();
        order.setStatus(OrderStatus.OUT_FOR_DELIVERY);

        orderRepository.save(order);

        Shipment savedShipment = shipmentRepository.save(shipment);

        return mapToResponse(savedShipment);
    }

    @Transactional
    public ShipmentResponse generateOtp(Long shipmentId) {

        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Shipment not found"));

        if (shipment.getStatus() != ShipmentStatus.OUT_FOR_DELIVERY) {
            throw new RuntimeException(
                    "Shipment is not out for delivery");
        }

        Random random = new Random();

        String otp = String.format(
                "%06d",
                random.nextInt(1_000_000)
        );

        shipment.setOtp(otp);
        shipment.setOtpGeneratedAt(LocalDateTime.now());
        shipment.setOtpAttempts(0);

        Shipment savedShipment = shipmentRepository.save(shipment);

        return mapToResponse(savedShipment);
    }

    @Transactional
    public ShipmentResponse verifyOtp(
            Long shipmentId,
            OtpVerificationRequest request) {

        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Shipment not found"));

        if (shipment.getOtp() == null) {
            throw new RuntimeException(
                    "OTP has not been generated");
        }

        if (shipment.getStatus() != ShipmentStatus.OUT_FOR_DELIVERY) {
            throw new RuntimeException(
                    "Shipment is not out for delivery");
        }

        if (request.getOtp() == null ||
                request.getOtp().isBlank()) {

            throw new RuntimeException("OTP is required");
        }

        int attempts = shipment.getOtpAttempts() + 1;
        shipment.setOtpAttempts(attempts);

        if (!shipment.getOtp().equals(request.getOtp())) {

            shipment.setStatus(ShipmentStatus.DELIVERY_ATTEMPTED);

            shipmentRepository.save(shipment);

            throw new RuntimeException("Invalid delivery OTP");
        }
        
        if (!shipment.getOtp().equals(request.getOtp())) {

            shipment.setStatus(ShipmentStatus.DELIVERY_ATTEMPTED);

            shipmentRepository.save(shipment);

            complaintService.createDeliveryComplaint(
                    shipment.getOrder().getCustomer().getId(),
                    shipment.getOrder().getId(),
                    shipment.getId(),
                    "Delivery OTP verification failed. Possible delivery dispute."
            );

            throw new RuntimeException("Invalid delivery OTP");
        }

        shipment.setStatus(ShipmentStatus.DELIVERED);
        shipment.setDeliveredAt(LocalDateTime.now());

        Order order = shipment.getOrder();
        order.setStatus(OrderStatus.DELIVERED);

        orderRepository.save(order);

        Shipment savedShipment = shipmentRepository.save(shipment);

        return mapToResponse(savedShipment);
    }

    private ShipmentResponse mapToResponse(Shipment shipment) {

        return new ShipmentResponse(
                shipment.getId(),
                shipment.getOrder().getId(),
                shipment.getStatus().name(),
                shipment.getOtp(),
                shipment.getOtpAttempts()
        );
    
    }
}