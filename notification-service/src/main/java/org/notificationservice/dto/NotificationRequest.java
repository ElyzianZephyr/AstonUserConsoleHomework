package org.notificationservice.dto;

public record NotificationRequest(
        String email,
        String operation
) {
}