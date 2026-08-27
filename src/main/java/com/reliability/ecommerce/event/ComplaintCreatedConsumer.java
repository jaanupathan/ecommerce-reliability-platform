package com.reliability.ecommerce.event;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ComplaintCreatedConsumer {

    @KafkaListener(
            topics = "complaint-created",
            groupId = "ecommerce-complaint-group"
    )
    public void consumeComplaintCreatedEvent(
            ComplaintCreatedEvent event) {

        System.out.println(
                "Complaint Created Event Received: "
                        + "complaintId=" + event.getComplaintId()
                        + ", orderId=" + event.getOrderId()
                        + ", shipmentId=" + event.getShipmentId()
                        + ", category=" + event.getCategory()
        );
    }
}