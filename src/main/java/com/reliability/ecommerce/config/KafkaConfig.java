
package com.reliability.ecommerce.config;

import com.reliability.ecommerce.event.ComplaintCreatedEvent;
import com.reliability.ecommerce.event.OrderCancelledEvent;
import com.reliability.ecommerce.event.OrderCreatedEvent;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    private static final String BOOTSTRAP_SERVERS =
            "localhost:9092";

    // =========================================================
    // COMPLAINT CREATED
    // =========================================================

    @Bean
    public NewTopic complaintCreatedTopic() {

        return TopicBuilder
                .name("complaint-created")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public ProducerFactory<String, ComplaintCreatedEvent>
    complaintCreatedProducerFactory() {

        Map<String, Object> config = new HashMap<>();

        config.put(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                BOOTSTRAP_SERVERS
        );

        config.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class
        );

        config.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                JsonSerializer.class
        );

        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate<String, ComplaintCreatedEvent>
    complaintCreatedKafkaTemplate() {

        return new KafkaTemplate<>(
                complaintCreatedProducerFactory()
        );
    }

    // =========================================================
    // ORDER CREATED
    // =========================================================

    @Bean
    public NewTopic orderCreatedTopic() {

        return TopicBuilder
                .name("order-created")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public ProducerFactory<String, OrderCreatedEvent>
    orderCreatedProducerFactory() {

        Map<String, Object> config = new HashMap<>();

        config.put(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                BOOTSTRAP_SERVERS
        );

        config.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class
        );

        config.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                JsonSerializer.class
        );

        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate<String, OrderCreatedEvent>
    orderCreatedKafkaTemplate() {

        return new KafkaTemplate<>(
                orderCreatedProducerFactory()
        );
    }

    // =========================================================
    // ORDER CANCELLED
    // =========================================================

    @Bean
    public NewTopic orderCancelledTopic() {

        return TopicBuilder
                .name("order-cancelled")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public ProducerFactory<String, OrderCancelledEvent>
    orderCancelledProducerFactory() {

        Map<String, Object> config = new HashMap<>();

        config.put(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                BOOTSTRAP_SERVERS
        );

        config.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class
        );

        config.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                JsonSerializer.class
        );

        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate<String, OrderCancelledEvent>
    orderCancelledKafkaTemplate() {

        return new KafkaTemplate<>(
                orderCancelledProducerFactory()
        );
    }

    // =========================================================
    // ORDER CREATED CONSUMER
    // =========================================================

    @Bean
    public ConsumerFactory<String, OrderCreatedEvent>
    consumerFactory() {

        JsonDeserializer<OrderCreatedEvent> deserializer =
                new JsonDeserializer<>(
                        OrderCreatedEvent.class
                );

        deserializer.addTrustedPackages(
                "com.reliability.ecommerce.event"
        );

        Map<String, Object> config = new HashMap<>();

        config.put(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                BOOTSTRAP_SERVERS
        );

        config.put(
                ConsumerConfig.GROUP_ID_CONFIG,
                "ecommerce-reliability-group"
        );

        config.put(
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class
        );

        config.put(
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                JsonDeserializer.class
        );

        return new DefaultKafkaConsumerFactory<>(
                config,
                new StringDeserializer(),
                deserializer
        );
    }
}

