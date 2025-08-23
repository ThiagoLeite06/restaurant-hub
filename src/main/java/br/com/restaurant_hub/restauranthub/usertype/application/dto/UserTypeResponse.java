package br.com.restaurant_hub.restauranthub.usertype.application.dto;

import java.time.LocalDateTime;

public record UserTypeResponse(
        String id,
        String name,
        String description
) {
}