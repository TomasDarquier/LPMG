package com.tdarquier.tfg.generation_service.dtos;

import java.util.List;

public record CodeGenerationDTO(
        String projectRDF,
        List<String> projectPoms
) {
}
