package br.com.restaurant_hub.restauranthub.common.dto;

import java.util.List;

public record ApiResponse<T>(
        List<T> data,
        PaginationResponse pagination
) {
}