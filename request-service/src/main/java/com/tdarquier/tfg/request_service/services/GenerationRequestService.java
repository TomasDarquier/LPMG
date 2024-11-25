package com.tdarquier.tfg.request_service.services;

import com.tdarquier.tfg.request_service.messages.GenerationRequest;

public interface GenerationRequestService {
    /**
     * Envía una solicitud de generación de proyecto si el usuario existe.
     * Primero verifica si el usuario está registrado, y si es así, registra la actividad y envía la solicitud de generación.
     *
     * @param generationRequest La solicitud de generación de proyecto que se enviará.
     * @param jwt El token JWT para la autenticación y autorización.
     * @return {@code true} si la solicitud se envió con éxito, {@code false} si el usuario no existe.
     */
    boolean sendGenerationRequest(GenerationRequest generationRequest, String jwt);
}
