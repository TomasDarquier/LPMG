package com.tdarquier.tfg.code_service.controllers;

import com.tdarquier.tfg.code_service.dtos.CodeGenerationDTO;
import com.tdarquier.tfg.code_service.entities.MinioFile;
import com.tdarquier.tfg.code_service.services.DinamicCodeGenerationService;
import com.tdarquier.tfg.code_service.services.MinioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/code")
public class CodeController {

    DinamicCodeGenerationService codeGenerationService;
    MinioService minioService;

    public CodeController(DinamicCodeGenerationService codeGenerationService, MinioService minioService) {
        this.codeGenerationService = codeGenerationService;
        this.minioService = minioService;
    }

    @PostMapping("/generate-code")
    ResponseEntity<Void> generateCode(@RequestParam CodeGenerationDTO dto){
        codeGenerationService.generateProjectCode(dto);
        return ResponseEntity.ok().build();
    }

}
