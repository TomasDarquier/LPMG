package com.tdarquier.tfg.code_service.services;

import java.util.List;

public interface OrchestrationService {
    /**
     * Orquesta la creacion del codigo de toda la arquitectura basado en los POMs proporcionados, el modelo RDF y el ID del usuario.
     *
     * @param poms Lista de cadenas que representan los POMs de los servicios.
     * @param rdfModel El modelo RDF en formato String.
     * @param userId El ID del usuario.
     */
    void createArchitectureCode(List<String> poms, String rdfModel, Long userId);
}
