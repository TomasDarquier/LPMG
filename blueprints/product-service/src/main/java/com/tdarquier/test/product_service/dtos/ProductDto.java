package com.tdarquier.test.product_service.dtos;

public record ProductDto(
        Long id,
        String name,
        Double price
) {
}
