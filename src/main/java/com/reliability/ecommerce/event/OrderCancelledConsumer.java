package com.reliability.ecommerce.event;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderCancelledConsumer {

    @KafkaListener(
            topics = "order-cancelled",
            groupId = "ecommerce-cancellation-group"
    )
    public void consumeOrderCancelledEvent(
            OrderCancelledEvent event) {

        System.out.println(
                "Order Cancelled Event Received: "
                        + "orderId=" + event.getOrderId()
                        + ", customerId=" + event.getCustomerId()
                        + ", reason=" + event.getReason()
        );
    }
}