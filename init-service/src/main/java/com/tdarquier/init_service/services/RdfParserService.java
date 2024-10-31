package com.tdarquier.init_service.services;

import com.tdarquier.init_service.entities.ProjectRequest;
import com.tdarquier.init_service.enums.Template;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RdfParserService {

    private static final String BASE_URI = "http://tomasdarquier.com/tfg/";

    public List<ProjectRequest> parseProjectRequest(String rdfData){
        Model model = ModelFactory.createDefaultModel();
        model.read(new java.io.StringReader(rdfData), null, "RDF/XML");

        List<String> serviceNames = listServiceNames(model);
        List<ProjectRequest> requests = new ArrayList<>();

        serviceNames.forEach(serviceName -> {
            Map<String,String> serviceData = getArtifactAndGroupIds(serviceName, model);

            requests.add(new ProjectRequest(
                    encodeForUrl(serviceName),
                    encodeForUrl(serviceData.get("groupId")),
                    encodeForUrl(serviceData.get("artifactId")),
                    getServiceDependencies(serviceName, model),
                    getTemplateType(serviceName, model)
                    ));
        });

        if(isConfigServerEnabled(model)){
            requests.add(createConfigServer(model));
        }
        if(isDiscoveryServerEnabled(model)){
            requests.add(createDiscoveryServer(model));
        }
        if(isGatewayEnabled(model)){
            requests.add(createGateway(model));
        }
        return requests;

    }

    private Template getTemplateType(String serviceName, Model model) {
        String queryStr = "SELECT ?template WHERE { "
                + "?service <" + BASE_URI + "hasService> ?serviceDesc . "
                + "?serviceDesc <" + BASE_URI + "serviceName> \"" + serviceName + "\" . "
                + "?serviceDesc <" + BASE_URI + "template> ?template . "
                + "} LIMIT 1";

        try (QueryExecution qExec = QueryExecutionFactory.create(QueryFactory.create(queryStr), model)) {
            ResultSet results = qExec.execSelect();
            if (results.hasNext()) {
                QuerySolution solution = results.nextSolution();
                String templateValue = solution.getLiteral("template").getString();

                for (Template template : Template.values()) {
                    if (template.name().equalsIgnoreCase(templateValue)) {
                        return template;
                    }
                }
            }
        }
        return null;
    }

    private String getServiceDependencies(String serviceName, Model model) {
        StringBuilder builder = new StringBuilder();
        if(isPartOfConnection(serviceName, model)){
            builder.append("cloud-feign,");
        }
        if(isDiscoveryServerEnabled(model)){
            builder.append("cloud-eureka,");
        }
        if(isConfigServerEnabled(model)){
            builder.append("cloud-config-client,");
        }
        return builder.toString();
    }

    private ProjectRequest createDiscoveryServer(Model model) {
        StringBuilder builder = new StringBuilder("cloud-eureka-server,");
        if(isConfigServerEnabled(model)){
            builder.append("cloud-config-client,");
        }
        return new ProjectRequest(
                "discovery-server",
                "com.discovery.server",
                "discovery-server",
                builder.toString(),
                Template.DISCOVERY_SERVICE_V1);
    }

    private ProjectRequest createGateway(Model model) {
        StringBuilder builder = new StringBuilder("cloud-gateway,");
        if(isConfigServerEnabled(model)){
            builder.append("cloud-config-client,");
        }
        if(isDiscoveryServerEnabled(model)){
            builder.append("cloud-eureka,");
        }
        return new ProjectRequest(
                "gateway",
                "com.api.gateway",
                "api-gateway",
                builder.toString(),
                Template.GATEWAY_SERVICE_V1);
    }

    private ProjectRequest createConfigServer(Model model) {
        StringBuilder builder = new StringBuilder("cloud-config-server,");
        if(isDiscoveryServerEnabled(model)){
            builder.append("cloud-eureka,");
        }
        return new ProjectRequest(
                "config-server",
                "com.config.server",
                "config-server",
                builder.toString(),
                Template.CONFIGURATION_SERVICE_V1);
    }

    public String encodeForUrl(String string){
        return string.replace(" ", "%20");
    }

    public List<String> listServiceNames(Model model) {

        List<String> serviceNames = new ArrayList<>();
        String serviceNamesQueryStr = "SELECT ?name WHERE { ?service <" + BASE_URI + "serviceName> ?name . }";

        try (QueryExecution qExec = QueryExecutionFactory.create(QueryFactory.create(serviceNamesQueryStr), model)) {
            ResultSet results = qExec.execSelect();
            while (results.hasNext()) {
                serviceNames.add(results.nextSolution().getLiteral("name").getString());
            }
        }
        return serviceNames;
    }
public Map<String, String> getArtifactAndGroupIds(String serviceName, Model model) {
    Map<String, String> ids = new HashMap<>();
    String serviceIdQueryStr = "SELECT ?artifactId ?groupId WHERE { "
            + "?service <" + BASE_URI + "hasService> ?serviceDesc . "
            + "?serviceDesc <" + BASE_URI + "serviceName> \"" + serviceName + "\" . "
            + "?serviceDesc <" + BASE_URI + "artifactId> ?artifactId . "
            + "?serviceDesc <" + BASE_URI + "groupId> ?groupId . }";

    try (QueryExecution qExec = QueryExecutionFactory.create(QueryFactory.create(serviceIdQueryStr), model)) {
        ResultSet results = qExec.execSelect();
        if (results.hasNext()) {
            QuerySolution solution = results.nextSolution();
            ids.put("artifactId", solution.getLiteral("artifactId").getString());
            ids.put("groupId", solution.getLiteral("groupId").getString());
        }
    }
    return ids;
}


    // Método que verifica si un servicio específico es parte de una conexión
    public boolean isPartOfConnection(String serviceName, Model model) {
        String queryOne = "ASK WHERE { ?connection <" + BASE_URI + "serviceOne> \"" + serviceName + "\" . }";
        String queryTwo = "ASK WHERE { ?connection <" + BASE_URI + "serviceTwo> \"" + serviceName + "\" . }";
        boolean partOfServiceOne = QueryExecutionFactory.create(QueryFactory.create(queryOne), model).execAsk();
        boolean partOfServiceTwo = QueryExecutionFactory.create(QueryFactory.create(queryTwo), model).execAsk();
        return partOfServiceOne || partOfServiceTwo;
    }

    public boolean isGatewayEnabled(Model model) {
        String queryStr = "ASK WHERE { "
                + "?project <" + BASE_URI + "hasInfrastructure> ?infrastructure . "
                + "?infrastructure <" + BASE_URI + "gatewayEnabled> \"true\" . }";

        return QueryExecutionFactory.create(QueryFactory.create(queryStr), model).execAsk();
    }

    public boolean isDiscoveryServerEnabled(Model model) {
        String queryStr = "ASK WHERE { "
                + "?project <" + BASE_URI + "hasInfrastructure> ?infrastructure . "
                + "?infrastructure <" + BASE_URI + "discoveryServerEnabled> \"true\" . }";

        return QueryExecutionFactory.create(QueryFactory.create(queryStr), model).execAsk();
    }

    public boolean isConfigServerEnabled(Model model) {
        String queryStr = "ASK WHERE { "
                + "?project <" + BASE_URI + "hasInfrastructure> ?infrastructure . "
                + "?infrastructure <" + BASE_URI + "configServerEnabled> \"true\" . }";

        return QueryExecutionFactory.create(QueryFactory.create(queryStr), model).execAsk();
    }


    //
    // TODO, definir seguridad en servicios
    //
    public String getSecurityType(Model model) {
        String queryStr = "SELECT ?securityType WHERE { "
                + "?project <" + BASE_URI + "hasSecurity> ?security . "
                + "?security <" + BASE_URI + "securityType> ?securityType . }";

        try (QueryExecution qExec = QueryExecutionFactory.create(QueryFactory.create(queryStr), model)) {
            ResultSet results = qExec.execSelect();
            if (results.hasNext()) {
                return results.nextSolution().getLiteral("securityType").getString();
            }
        }
        return "none";  // Valor predeterminado si no se encuentra un tipo de seguridad
    }

}
