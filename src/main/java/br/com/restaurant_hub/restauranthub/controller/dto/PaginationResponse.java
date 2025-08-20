package br.com.restaurant_hub.restauranthub.controller.dto;

public record PaginationResponse(Integer page, Integer pageSize, Long totalElements, Integer totalPages) {
}
