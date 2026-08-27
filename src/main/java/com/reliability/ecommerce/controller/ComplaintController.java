package com.reliability.ecommerce.controller;

import com.reliability.ecommerce.dto.ComplaintRequest;
import com.reliability.ecommerce.dto.ComplaintResponse;
import com.reliability.ecommerce.dto.ComplaintStatusRequest;
import com.reliability.ecommerce.service.ComplaintService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/complaints")
public class ComplaintController {

    private final ComplaintService complaintService;

    public ComplaintController(
            ComplaintService complaintService) {

        this.complaintService = complaintService;
    }

    @PostMapping
    public ResponseEntity<ComplaintResponse> createComplaint(
            @RequestBody ComplaintRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(complaintService.createComplaint(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComplaintResponse> getComplaint(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                complaintService.getComplaint(id)
        );
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<ComplaintResponse>>
    getComplaintsByStatus(
            @PathVariable String status) {

        return ResponseEntity.ok(
                complaintService.getComplaintsByStatus(status)
        );
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ComplaintResponse>
    updateComplaintStatus(
            @PathVariable Long id,
            @RequestBody ComplaintStatusRequest request) {

        return ResponseEntity.ok(
                complaintService.updateComplaintStatus(
                        id,
                        request.getStatus()
                )
        );
    }
}