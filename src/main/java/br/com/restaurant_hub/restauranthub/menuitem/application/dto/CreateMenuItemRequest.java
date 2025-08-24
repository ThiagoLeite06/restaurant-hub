package br.com.restaurant_hub.restauranthub.menuitem.application.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreateMenuItemRequest(
    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    String name,
    
    @NotBlank(message = "Descrição é obrigatória")
    @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
    String description,
    
    @NotNull(message = "Preço é obrigatório")
    @DecimalMin(value = "0.01", message = "Preço deve ser maior que zero")
    BigDecimal price,
    
    Boolean availableOnlyInRestaurant,
    
    @Size(max = 500, message = "Caminho da foto deve ter no máximo 500 caracteres")
    String photoPath,
    
    @NotNull(message = "ID do restaurante é obrigatório")
    Long restaurantId
) {}