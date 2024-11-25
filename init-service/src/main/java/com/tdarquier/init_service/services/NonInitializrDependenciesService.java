package com.tdarquier.init_service.services;

import com.tdarquier.init_service.enums.Template;

import java.util.List;

public interface NonInitializrDependenciesService {
    /**
     * Obtiene las dependencias presentes en InitializrApi asociadas a un template específico. Las dependencias se definen para cada tipo
     * de servicio (por ejemplo, usuario, carrito, pedidos) y se retornan en una lista de cadenas.
     *
     * @param template El template que representa un tipo de servicio.
     * @return Una lista de dependencias asociadas al template dado.
     */
    List<String> getDependenciesByTemplate(Template template);

    /**
     * Inserta las dependencias adicionales en el archivo POM proporcionado. Las dependencias adicionales se extraen
     * de los archivos definidos por el servicio y se insertan en la sección `<dependencies>` del POM.
     *
     * @param pom El archivo POM original.
     * @param extraDependencies Una lista de dependencias adicionales que deben agregarse al POM.
     * @return El archivo POM con las dependencias adicionales insertadas en su lugar correspondiente.
     */
    String getCompletePom(String pom, List<String> extraDependencies);
}
