package com.tdarquier.tfg.generation_service.clients;

import com.tdarquier.tfg.generation_service.dtos.CodeGenerationDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "code-service")
@Component
public interface CodeClient {

    @PostMapping("/code/generate-code")
    Void generateCode(@RequestParam CodeGenerationDTO dto);
}
