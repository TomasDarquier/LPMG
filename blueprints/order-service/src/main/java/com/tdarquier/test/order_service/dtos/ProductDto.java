package com.tdarquier.test.order_service.dtos;

public record ProductDto(
        Long id,
        String name,
        Double price
) {
}
