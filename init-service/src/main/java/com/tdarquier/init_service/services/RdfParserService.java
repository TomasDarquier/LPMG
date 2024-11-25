package com.tdarquier.init_service.services;

import com.tdarquier.init_service.entities.ProjectRequest;
import com.tdarquier.init_service.enums.Template;
import org.apache.jena.rdf.model.Model;

import java.util.List;
import java.util.Map;

public interface RdfParserService {

    /**
     * Parsear un documento RDF en formato XML y extraer la información relevante para crear una lista de solicitudes de proyectos.
     *
     * @param rdfData El contenido RDF en formato XML que representa la información del proyecto.
     * @return Una lista de {@link ProjectRequest} que contiene la configuración y dependencias de los servicios del proyecto.
     */
    List<ProjectRequest> parseProjectRequest(String rdfData);

    /**
     * Obtiene el tipo de plantilla para un servicio específico desde el modelo RDF.
     *
     * @param serviceName El nombre del servicio cuyo tipo de plantilla se desea obtener.
     * @param model El modelo RDF que contiene los datos del proyecto.
     * @return El tipo de plantilla del servicio como una instancia de {@link Template}, o {@code null} si no se encuentra.
     */
    Template getTemplateType(String serviceName, Model model);

    /**
     * Obtiene una lista de nombres de servicios desde el modelo RDF.
     *
     * @param model El modelo RDF que contiene los datos del proyecto.
     * @return Una lista de cadenas que representan los nombres de los servicios encontrados en el RDF.
     */
    List<String> listServiceNames(Model model);

    /**
     * Obtiene los identificadores de artefacto y grupo de un servicio específico desde el modelo RDF.
     *
     * @param serviceName El nombre del servicio cuyo artefacto y grupo se desean obtener.
     * @param model El modelo RDF que contiene los datos del proyecto.
     * @return Un mapa con las claves "artifactId" y "groupId" que representan los identificadores del servicio.
     */
    Map<String, String> getArtifactAndGroupIds(String serviceName, Model model);

    /**
     * Verifica si un servicio es parte de alguna conexión en el modelo RDF.
     *
     * @param serviceName El nombre del servicio que se desea verificar.
     * @param model El modelo RDF que contiene los datos del proyecto.
     * @return {@code true} si el servicio está involucrado en alguna conexión, {@code false} en caso contrario.
     */
    boolean isPartOfConnection(String serviceName, Model model);

    /**
     * Verifica si la infraestructura del proyecto incluye un servidor de descubrimiento.
     *
     * @param model El modelo RDF que contiene los datos del proyecto.
     * @return {@code true} si el servidor de descubrimiento está habilitado, {@code false} en caso contrario.
     */
    boolean isDiscoveryServerEnabled(Model model);

    /**
     * Verifica si la infraestructura del proyecto incluye un servidor de configuración.
     *
     * @param model El modelo RDF que contiene los datos del proyecto.
     * @return {@code true} si el servidor de configuración está habilitado, {@code false} en caso contrario.
     */
    boolean isConfigServerEnabled(Model model);

    /**
     * Verifica si la infraestructura del proyecto incluye una puerta de enlace (Gateway).
     *
     * @param model El modelo RDF que contiene los datos del proyecto.
     * @return {@code true} si el gateway está habilitado, {@code false} en caso contrario.
     */
    boolean isGatewayEnabled(Model model);

    /**
     * Obtiene el tipo de seguridad configurado en el proyecto desde el modelo RDF.
     *
     * @param model El modelo RDF que contiene los datos del proyecto.
     * @return El tipo de seguridad configurado en el proyecto, o "none" si no se ha configurado seguridad.
     */
    String getSecurityType(Model model);
}
