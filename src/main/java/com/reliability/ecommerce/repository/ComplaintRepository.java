package com.reliability.ecommerce.repository;

import com.reliability.ecommerce.entity.Complaint;
import com.reliability.ecommerce.entity.ComplaintStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ComplaintRepository
        extends JpaRepository<Complaint, Long> {

    List<Complaint> findByCustomerId(Long customerId);

    List<Complaint> findByStatus(ComplaintStatus status);

    List<Complaint> findByOrderId(Long orderId);

    @Query("""
        SELECT DISTINCT c
        FROM Complaint c
        JOIN c.order o
        JOIN o.items oi
        JOIN oi.product p
        WHERE p.seller.id = :sellerId
        """)
    List<Complaint> findComplaintsBySellerId(
            @Param("sellerId") Long sellerId);
}