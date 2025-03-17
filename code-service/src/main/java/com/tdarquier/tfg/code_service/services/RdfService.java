package com.tdarquier.tfg.code_service.services;

import com.tdarquier.tfg.code_service.entities.Connection;
import com.tdarquier.tfg.code_service.enums.PersistenceType;
import com.tdarquier.tfg.code_service.enums.Template;
import org.apache.jena.rdf.model.Model;

import java.util.List;
import java.util.Map;

public interface RdfService {

    /**
     * Obtiene el tipo de template de un servicio a partir del nombre del servicio.
     *
     * @param serviceName El nombre del servicio
     * @param model El modelo RDF
     * @return El template correspondiente o null si no se encuentra
     */
    Template getTemplateType(String serviceName, Model model);

    /**
     * Obtiene los ArtifactId y GroupId de un servicio.
     *
     * @param serviceName El nombre del servicio
     * @param model El modelo RDF
     * @return Un mapa con el ArtifactId y GroupId
     */
    Map<String, String> getArtifactAndGroupIds(String serviceName, Model model);

    /**
     * Verifica si un servicio está involucrado en alguna conexión.
     *
     * @param serviceName El nombre del servicio
     * @param model El modelo RDF
     * @return true si está involucrado en una conexión, false de lo contrario
     */
    boolean isPartOfConnection(String serviceName, Model model);

    /**
     * Obtiene los nombres de los servicios conectados a un servicio dado.
     *
     * @param serviceName El nombre del servicio
     * @param model El modelo RDF
     * @return Una lista de nombres de servicios conectados
     */
    List<String> getConnectionNames(String serviceName, Model model);

    /**
     * Verifica si el servidor de descubrimiento está habilitado en la infraestructura.
     *
     * @param model El modelo RDF
     * @return true si está habilitado, false de lo contrario
     */
    boolean isDiscoveryServerEnabled(Model model);

    /**
     * Verifica si el servidor de configuración está habilitado en la infraestructura.
     *
     * @param model El modelo RDF
     * @return true si está habilitado, false de lo contrario
     */
    boolean isConfigServerEnabled(Model model);

    /**
     * Obtiene el puerto de un servicio.
     *
     * @param model El modelo RDF
     * @param serviceName El nombre del servicio
     * @return El puerto del servicio o null si no se encuentra
     */
    String getServicePort(Model model, String serviceName);

    /**
     * Verifica si el gateway está habilitado en la infraestructura.
     *
     * @param model El modelo RDF
     * @return true si está habilitado, false de lo contrario
     */
    boolean isGatewayEnabled(Model model);

    /**
     * Obtiene el tipo de persistencia de un servicio.
     *
     * @param model El modelo RDF
     * @param serviceName El nombre del servicio
     * @return El tipo de persistencia o null si no se encuentra
     */
    PersistenceType getPersistenceType(Model model, String serviceName);

    /**
     * Obtiene las conexiones de un servicio dado, con la información detallada.
     *
     * @param name El nombre del servicio
     * @param model El modelo RDF
     * @return Una lista de objetos de conexión, o null si no tiene conexiones
     */
    List<Connection> getConnections(String name, Model model);

    /**
     * Obtiene el prefijo de la ruta API de un servicio.
     *
     * @param name El nombre del servicio
     * @param model El modelo RDF
     * @return El prefijo de la ruta API o null si no se encuentra
     */
    String getApiPathPrefix(String name, Model model);

    boolean usesGraphQL(String serviceName, Model model);
    boolean usesREST(String serviceName, Model model);
}
