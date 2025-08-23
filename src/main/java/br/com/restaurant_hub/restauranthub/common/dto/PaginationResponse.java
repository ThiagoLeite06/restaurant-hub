package br.com.restaurant_hub.restauranthub.common.dto;

public record PaginationResponse(
        Integer page,
        Integer pageSize,
        Long totalElements,
        Integer totalPages
) {
}