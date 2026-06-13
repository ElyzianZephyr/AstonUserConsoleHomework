package org.userservice.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class UserEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendUserEvent(String operation, String email) {
        Map<String, String> event = Map.of(
                "operation", operation,
                "email", email
        );
        kafkaTemplate.send("user-events", event);
    }
}