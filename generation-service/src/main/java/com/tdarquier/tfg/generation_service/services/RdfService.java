package com.tdarquier.tfg.generation_service.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tdarquier.tfg.generation_service.messages.GenerationRequest;
import org.springframework.scheduling.annotation.Async;

import java.util.concurrent.CompletableFuture;

public interface RdfService {
    /**
     * Convierte la información de un proyecto y sus componentes (servicios, conexiones, infraestructura y seguridad)
     * a un documento RDF en formato XML.
     * Este proceso es asíncrono, y se ejecuta en segundo plano para no bloquear el hilo principal de ejecución.
     *
     * @param generationRequest El objeto que contiene la solicitud de generación, que incluye los detalles del proyecto
     *                         en formato JSON.
     * @return Un futuro que se completa con el contenido RDF en formato XML.
     * @throws JsonProcessingException Si ocurre un error durante el procesamiento del JSON de entrada.
     */
    @Async
    CompletableFuture<String> toRdf(GenerationRequest generationRequest) throws JsonProcessingException;
}