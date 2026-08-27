package com.reliability.ecommerce.service;

import com.reliability.ecommerce.dto.SellerReliabilityResponse;
import com.reliability.ecommerce.entity.Complaint;
import com.reliability.ecommerce.entity.Order;
import com.reliability.ecommerce.entity.OrderStatus;
import com.reliability.ecommerce.entity.Seller;
import com.reliability.ecommerce.exception.ResourceNotFoundException;
import com.reliability.ecommerce.repository.ComplaintRepository;
import com.reliability.ecommerce.repository.OrderRepository;
import com.reliability.ecommerce.repository.SellerRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SellerReliabilityService {

    private final SellerRepository sellerRepository;
    private final OrderRepository orderRepository;
    private final ComplaintRepository complaintRepository;

    public SellerReliabilityService(
            SellerRepository sellerRepository,
            OrderRepository orderRepository,
            ComplaintRepository complaintRepository) {

        this.sellerRepository = sellerRepository;
        this.orderRepository = orderRepository;
        this.complaintRepository = complaintRepository;
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "sellerReliability", key = "#sellerId")
    public SellerReliabilityResponse calculateReliability(
            Long sellerId) {

        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Seller not found"));

        List<Order> orders =
                orderRepository.findOrdersBySellerId(sellerId);

        List<Complaint> complaints =
                complaintRepository.findComplaintsBySellerId(sellerId);

        int totalOrders = orders.size();

        int cancelledOrders = 0;

        for (Order order : orders) {
            if (order.getStatus() == OrderStatus.CANCELLED) {
                cancelledOrders++;
            }
        }

        int complaintCount = complaints.size();

        double cancellationRate = 0.0;
        double complaintRate = 0.0;

        if (totalOrders > 0) {
            cancellationRate =
                    (cancelledOrders * 100.0) / totalOrders;

            complaintRate =
                    (complaintCount * 100.0) / totalOrders;
        }

        double returnRate =
                seller.getReturnRate() == null
                        ? 0.0
                        : seller.getReturnRate();

        double trustScore =
                100.0
                        - (cancellationRate * 0.5)
                        - complaintRate
                        - (returnRate * 0.5);

        if (trustScore < 0.0) {
            trustScore = 0.0;
        }

        if (trustScore > 100.0) {
            trustScore = 100.0;
        }

        return new SellerReliabilityResponse(
                seller.getId(),
                seller.getBusinessName(),
                totalOrders,
                cancelledOrders,
                complaintCount,
                cancellationRate,
                returnRate,
                complaintRate,
                trustScore
        );
    }

    @CacheEvict(value = "sellerReliability", key = "#sellerId")
    public void clearReliabilityCache(Long sellerId) {
        // Cache is cleared by Spring.
    }
}