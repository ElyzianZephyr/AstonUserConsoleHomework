package org.notificationservice.listener;

import lombok.RequiredArgsConstructor;
import org.notificationservice.dto.UserEvent;
import org.notificationservice.service.EmailService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserEventListener {

    private final EmailService emailService;

    @KafkaListener(topics = "user-events", groupId = "notification-group")
    public void handleUserEvent(UserEvent event) {
        emailService.sendNotification(event.email(), event.operation());
    }
}