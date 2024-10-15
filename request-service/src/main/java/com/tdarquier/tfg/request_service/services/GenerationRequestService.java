package com.tdarquier.tfg.request_service.services;

import com.tdarquier.tfg.request_service.messages.GenerationRequest;

public interface GenerationRequestService {
    boolean sendGenerationRequest(GenerationRequest generationRequest);
}
