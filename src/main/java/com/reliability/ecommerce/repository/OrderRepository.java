package com.reliability.ecommerce.repository;

import com.reliability.ecommerce.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByCustomerId(Long customerId);

    @Query("""
        SELECT DISTINCT o
        FROM Order o
        JOIN o.items oi
        JOIN oi.product p
        WHERE p.seller.id = :sellerId
        """)
    List<Order> findOrdersBySellerId(
            @Param("sellerId") Long sellerId);
}