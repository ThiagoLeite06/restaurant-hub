package br.com.restaurant_hub.restauranthub.model.dto;

import java.util.Date;

public record UserResponse(
        Long id,
        String name,
        String email,
        String login,
        Date lastUpdated,
        String address
) {}
