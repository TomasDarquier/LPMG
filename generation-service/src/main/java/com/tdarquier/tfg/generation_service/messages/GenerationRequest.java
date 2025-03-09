package com.tdarquier.tfg.generation_service.messages;

public record GenerationRequest(
        Long userId,
        String servicesJson
){
}
