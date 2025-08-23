package br.com.restaurant_hub.restauranthub.usertype.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserTypeRequest(
        @NotBlank(message = "Nome do tipo de usuário é obrigatório")
        @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
        String name,
        
        @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
        String description
) {
}