package br.com.restaurant_hub.restauranthub.user.presentation.dto;

import java.util.List;

public record ApiResponse<T>(
        List<T> data,
        PaginationResponse pagination
) {
}