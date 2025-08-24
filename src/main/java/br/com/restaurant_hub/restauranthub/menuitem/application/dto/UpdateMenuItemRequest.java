package br.com.restaurant_hub.restauranthub.menuitem.application.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record UpdateMenuItemRequest(
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    String name,
    
    @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
    String description,
    
    @DecimalMin(value = "0.01", message = "Preço deve ser maior que zero")
    BigDecimal price,
    
    Boolean availableOnlyInRestaurant,
    
    @Size(max = 500, message = "Caminho da foto deve ter no máximo 500 caracteres")
    String photoPath,
    
    Boolean active
) {}