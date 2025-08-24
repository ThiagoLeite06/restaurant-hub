package br.com.restaurant_hub.restauranthub.restaurant.application.dto;

import br.com.restaurant_hub.restauranthub.restaurant.domain.enums.CuisineType;

import java.time.LocalDateTime;

public record RestaurantResponse(
        String id,
        String name,
        String address,
        CuisineType cuisineType,
        String cuisineTypeName,
        String openingHours,
        String ownerId,
        String ownerName,
        Boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}