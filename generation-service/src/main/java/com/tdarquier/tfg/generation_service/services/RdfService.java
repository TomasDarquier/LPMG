package com.tdarquier.tfg.generation_service.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tdarquier.tfg.generation_service.kafka.generation.GenerationRequest;

import java.util.concurrent.CompletableFuture;

public interface RdfService {
    CompletableFuture<String> toRdf(GenerationRequest generationRequest) throws JsonProcessingException;
}