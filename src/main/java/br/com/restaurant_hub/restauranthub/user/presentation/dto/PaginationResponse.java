package br.com.restaurant_hub.restauranthub.user.presentation.dto;

public record PaginationResponse(
        Integer page,
        Integer pageSize,
        Long totalElements,
        Integer totalPages
) {
}