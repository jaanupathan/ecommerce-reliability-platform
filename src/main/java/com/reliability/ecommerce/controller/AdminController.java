package com.reliability.ecommerce.controller;

import com.reliability.ecommerce.dto.ComplaintResponse;
import com.reliability.ecommerce.dto.SellerReliabilityResponse;
import com.reliability.ecommerce.service.ComplaintService;
import com.reliability.ecommerce.service.SellerReliabilityService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final ComplaintService complaintService;
    private final SellerReliabilityService sellerReliabilityService;

    public AdminController(
            ComplaintService complaintService,
            SellerReliabilityService sellerReliabilityService) {

        this.complaintService = complaintService;
        this.sellerReliabilityService = sellerReliabilityService;
    }

    // =========================================================
    // Get all OPEN complaints
    // GET /api/admin/complaints
    // =========================================================

    @GetMapping("/complaints")
    public ResponseEntity<List<ComplaintResponse>> getOpenComplaints() {

        return ResponseEntity.ok(
                complaintService.getComplaintsByStatus("OPEN")
        );
    }

    // =========================================================
    // Get complaints by status
    // GET /api/admin/complaints/{status}
    // Example:
    // GET /api/admin/complaints/OPEN
    // GET /api/admin/complaints/RESOLVED
    // =========================================================

    @GetMapping("/complaints/{status}")
    public ResponseEntity<List<ComplaintResponse>> getComplaintsByStatus(
            @PathVariable String status) {

        return ResponseEntity.ok(
                complaintService.getComplaintsByStatus(status)
        );
    }

    // =========================================================
    // Resolve complaint
    // PUT /api/admin/complaints/{id}/resolve
    // =========================================================

    @PutMapping("/complaints/{id}/resolve")
    public ResponseEntity<ComplaintResponse> resolveComplaint(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                complaintService.updateComplaintStatus(
                        id,
                        "RESOLVED"
                )
        );
    }

    // =========================================================
    // Get seller reliability
    // GET /api/admin/sellers/{sellerId}/reliability
    // =========================================================

    @GetMapping("/sellers/{sellerId}/reliability")
    public ResponseEntity<SellerReliabilityResponse>
    getSellerReliability(
            @PathVariable Long sellerId) {

        return ResponseEntity.ok(
                sellerReliabilityService
                        .calculateReliability(sellerId)
        );
    }
}

