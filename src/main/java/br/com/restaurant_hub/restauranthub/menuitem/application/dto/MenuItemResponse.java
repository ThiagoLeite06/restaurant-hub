package br.com.restaurant_hub.restauranthub.menuitem.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MenuItemResponse(
    String id,
    String name,
    String description,
    BigDecimal price,
    Boolean availableOnlyInRestaurant,
    String photoPath,
    String restaurantId,
    String restaurantName,
    Boolean active,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}