package org.userservice.dto;


public record UserResponse(
        Long id,
        String name,
        Integer age,
        String email
) {
}