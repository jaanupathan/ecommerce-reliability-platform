package com.reliability.ecommerce.event;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private static final String COMPLAINT_CREATED_TOPIC =
            "complaint-created";

    private static final String ORDER_CREATED_TOPIC =
            "order-created";

    private static final String ORDER_CANCELLED_TOPIC =
            "order-cancelled";

    private final KafkaTemplate<String, ComplaintCreatedEvent>
            complaintCreatedKafkaTemplate;

    private final KafkaTemplate<String, OrderCreatedEvent>
            orderCreatedKafkaTemplate;

    private final KafkaTemplate<String, OrderCancelledEvent>
            orderCancelledKafkaTemplate;

    public KafkaProducerService(
            KafkaTemplate<String, OrderCreatedEvent> orderCreatedKafkaTemplate,
            KafkaTemplate<String, OrderCancelledEvent> orderCancelledKafkaTemplate,
            KafkaTemplate<String, ComplaintCreatedEvent> complaintCreatedKafkaTemplate) {

        this.orderCreatedKafkaTemplate =
                orderCreatedKafkaTemplate;

        this.orderCancelledKafkaTemplate =
                orderCancelledKafkaTemplate;

        this.complaintCreatedKafkaTemplate =
                complaintCreatedKafkaTemplate;
    }

    public void sendComplaintCreatedEvent(
            ComplaintCreatedEvent event) {

        complaintCreatedKafkaTemplate.send(
                COMPLAINT_CREATED_TOPIC,
                String.valueOf(event.getComplaintId()),
                event
        );
    }

    public void sendOrderCreatedEvent(
            OrderCreatedEvent event) {

        orderCreatedKafkaTemplate.send(
                ORDER_CREATED_TOPIC,
                String.valueOf(event.getOrderId()),
                event
        );
    }

    public void sendOrderCancelledEvent(
            OrderCancelledEvent event) {

        orderCancelledKafkaTemplate.send(
                ORDER_CANCELLED_TOPIC,
                String.valueOf(event.getOrderId()),
                event
        );
    }
}