package com.tdarquier.tfg.request_service.controllers;


import com.tdarquier.tfg.request_service.messages.GenerationRequest;
import com.tdarquier.tfg.request_service.services.GenerationRequestService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/generation")
@RequiredArgsConstructor
public class GenerationRequestController {

    private final GenerationRequestService generationRequestService;

    @PostMapping("/request/{id}")
    public Boolean requestCodeGeneration(@RequestParam String json, @PathVariable Long id){
        GenerationRequest generationRequest = new GenerationRequest(id, json);
        return generationRequestService.sendGenerationRequest(generationRequest);
    }
}
