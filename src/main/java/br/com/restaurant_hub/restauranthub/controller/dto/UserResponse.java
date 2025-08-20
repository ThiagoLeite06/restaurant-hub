package br.com.restaurant_hub.restauranthub.controller.dto;

public record UserResponse(
                Long id,
                String name,
                String email,
                String login,
                String address) {
}
