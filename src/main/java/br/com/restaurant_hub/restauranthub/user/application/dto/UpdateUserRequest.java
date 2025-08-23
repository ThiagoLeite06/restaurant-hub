package br.com.restaurant_hub.restauranthub.user.application.dto;

import jakarta.validation.constraints.Size;

public record UpdateUserRequest(
        @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
        String name,
        
        @Size(max = 500, message = "Endereço deve ter no máximo 500 caracteres")
        String address
) {
}