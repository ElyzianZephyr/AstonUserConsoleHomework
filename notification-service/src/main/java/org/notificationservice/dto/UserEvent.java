package org.notificationservice.dto;

public record UserEvent(
        String operation,
        String email
) {
}