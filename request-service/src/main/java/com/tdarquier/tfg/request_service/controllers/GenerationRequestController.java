package com.tdarquier.tfg.request_service.controllers;


import com.tdarquier.tfg.request_service.kafka.SGSProducer;
import com.tdarquier.tfg.request_service.messages.GenerationRequest;
import com.tdarquier.tfg.request_service.messages.GenerationStatus;
import com.tdarquier.tfg.request_service.services.GenerationRequestService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/generation")
@RequiredArgsConstructor
public class GenerationRequestController {

    private final GenerationRequestService generationRequestService;
    private final SGSProducer sgsProducer;

    @PostMapping("/request/{id}")
    public Boolean requestCodeGeneration(@RequestParam String json, @PathVariable Long id, HttpServletRequest request) {

        // create generation request
        GenerationRequest generationRequest = new GenerationRequest(id, json);
        String jwt = request.getHeader(HttpHeaders.AUTHORIZATION);

        // create and send generation status for progress bar
        GenerationStatus generationStatus = new GenerationStatus(
                id,
               "Solicitud Enviada",
               1,
                new Date()
        );
        sgsProducer.sendGenerationStatus(generationStatus, jwt);

        return generationRequestService.sendGenerationRequest(generationRequest, jwt);
    }
}
