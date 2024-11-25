package com.tdarquier.init_service.services;

import java.util.List;

public interface ProjectGenerationService {

    /**
     * Genera el proyecto a partir de un documento RDF, el cual contiene información sobre los servicios y sus
     * dependencias. Este métod procesa los componentes del proyecto y genera los archivos POM correspondientes,
     * añadiendo las dependencias necesarias.
     *
     * @param rdf El contenido RDF en formato XML que describe los componentes del proyecto.
     * @return Una lista de cadenas que representan los POMs generados para los diferentes componentes del proyecto.
     */
    List<String> generateProject(String rdf);
}
