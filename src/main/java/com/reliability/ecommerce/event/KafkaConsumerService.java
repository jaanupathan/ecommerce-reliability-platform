package com.reliability.ecommerce.event;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @KafkaListener(
            topics = "order-created",
            groupId = "ecommerce-reliability-group"
    )
    public void consumeOrderCreatedEvent(
            OrderCreatedEvent event) {

        System.out.println(
                "Order Created Event Received: "
                        + "orderId=" + event.getOrderId()
                        + ", customerId=" + event.getCustomerId()
                        + ", totalAmount=" + event.getTotalAmount()
        );
    }
}