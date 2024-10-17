package com.tdarquier.tfg.generation_service.kafka.generation;

public record GenerationRequest(
        Long userId,
        String servicesJson
){
}
