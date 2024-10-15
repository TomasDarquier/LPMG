package com.tdarquier.tfg.request_service.messages;

public record GenerationRequest(
        Long userId,
        String servicesJson
){
}
