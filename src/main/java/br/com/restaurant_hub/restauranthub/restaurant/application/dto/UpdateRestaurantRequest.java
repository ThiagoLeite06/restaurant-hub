package br.com.restaurant_hub.restauranthub.restaurant.application.dto;

import br.com.restaurant_hub.restauranthub.restaurant.domain.enums.CuisineType;
import jakarta.validation.constraints.Size;

public record UpdateRestaurantRequest(
        @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
        String name,
        
        @Size(min = 10, max = 500, message = "Endereço deve ter entre 10 e 500 caracteres")
        String address,
        
        CuisineType cuisineType,
        
        @Size(max = 100, message = "Horário de funcionamento deve ter no máximo 100 caracteres")
        String openingHours
) {
}