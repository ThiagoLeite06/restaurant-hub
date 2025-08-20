package br.com.restaurant_hub.restauranthub.controller.dto;

public record UpdateUserRequest(String name, String email, String login, String address) {
}
