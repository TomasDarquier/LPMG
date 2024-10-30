package com.tdarquier.tfg.code_service.dtos;

import java.util.List;

public record CodeGenerationDTO(
        String projectRDF,
        List<String> projectPoms
) {
}
