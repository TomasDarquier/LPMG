package com.tdarquier.tfg.code_service.controllers;

import com.tdarquier.tfg.code_service.dtos.CodeGenerationDTO;
import com.tdarquier.tfg.code_service.services.DynamicCodeGenerationServiceImpl;
import com.tdarquier.tfg.code_service.services.MinioServiceImpl;
import com.tdarquier.tfg.code_service.services.OrchestrationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/code")
public class CodeController {

    private final DynamicCodeGenerationServiceImpl codeGenerationService;
    private final MinioServiceImpl minioService;
    private final OrchestrationServiceImpl orchestationService;

    @PostMapping("/generate-code/{id}")
    ResponseEntity<Void> generateCode(@RequestBody CodeGenerationDTO dto, @PathVariable String id){
        orchestationService.createArchitectureCode(dto.projectPoms(),dto.projectRDF(), Long.valueOf(id));
        System.out.println(dto.projectRDF());
        return ResponseEntity.ok().build();
    }

}
