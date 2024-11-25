package com.tdarquier.tfg.code_service.services;

import com.tdarquier.tfg.code_service.entities.ComponentData;

public interface DynamicCodeGenerationService {

    /**
     * Genera el código del servicio basado en los datos proporcionados del componente y sube los archivos generados
     * al bucket especificado. Esto incluye archivos Java, archivos de propiedades, y posiblemente un Dockerfile,
     * dependiendo de la plantilla del servicio.
     *
     * @param componentData los datos relacionados con el componente (por ejemplo, nombre del servicio, plantilla, conexiones, etc.)
     * @param bucket el nombre del bucket donde se guardarán los archivos generados
     */
    void generateServiceCode(ComponentData componentData, String bucket);
}
