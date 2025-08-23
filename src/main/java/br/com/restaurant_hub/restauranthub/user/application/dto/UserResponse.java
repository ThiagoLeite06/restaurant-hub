package br.com.restaurant_hub.restauranthub.user.application.dto;

import java.time.LocalDateTime;

public record UserResponse(
        String id,
        String name,
        String email,
        String login,
        String address,
        String userTypeId,
        String userTypeName,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Boolean enabled
) {
}