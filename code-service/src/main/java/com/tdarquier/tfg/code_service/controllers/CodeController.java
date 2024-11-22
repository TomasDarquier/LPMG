package com.tdarquier.tfg.code_service.controllers;

import com.tdarquier.tfg.code_service.dtos.CodeGenerationDTO;
import com.tdarquier.tfg.code_service.services.DynamicCodeGenerationService;
import com.tdarquier.tfg.code_service.services.MinioService;
import com.tdarquier.tfg.code_service.services.OrchestationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/code")
public class CodeController {

    DynamicCodeGenerationService codeGenerationService;
    MinioService minioService;
    OrchestationService orchestationService;

    public CodeController(DynamicCodeGenerationService codeGenerationService, MinioService minioService, OrchestationService orchestationService) {
        this.codeGenerationService = codeGenerationService;
        this.minioService = minioService;
        this.orchestationService = orchestationService;
    }

    @PostMapping("/generate-code/{id}")
    ResponseEntity<Void> generateCode(@RequestBody CodeGenerationDTO dto, @PathVariable String id){
        orchestationService.createArchitectureCode(dto.projectPoms(),dto.projectRDF(), Long.valueOf(id));
        return ResponseEntity.ok().build();
    }

}
