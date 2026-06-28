package org.notificationservice.controller;

import lombok.RequiredArgsConstructor;
import org.notificationservice.dto.NotificationRequest;
import org.notificationservice.service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<Void> sendEmail(@RequestBody NotificationRequest request) {
        emailService.sendNotification(request.email(), request.operation());
        return ResponseEntity.ok().build();
    }
}