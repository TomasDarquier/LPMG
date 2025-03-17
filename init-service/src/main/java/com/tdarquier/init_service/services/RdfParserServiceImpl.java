package com.tdarquier.init_service.services;

import com.tdarquier.init_service.entities.ProjectRequest;
import com.tdarquier.init_service.enums.Persistence;
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
public class RdfParserServiceImpl implements RdfParserService{

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

    public Template getTemplateType(String serviceName, Model model) {
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
            if(isAnyConnectedServiceNotificationService(serviceName, model)){
                builder.append("kafka,");
            }
        }
        if(isDiscoveryServerEnabled(model)){
            builder.append("cloud-eureka,");
        }
        if(isConfigServerEnabled(model)){
            builder.append("cloud-config-client,");
        }
        // TODO
        // tambien hacer si usa rest para agregar las dependencias
        // que no esten x default
        if(usesGraphQL(serviceName, model)){
            builder.append("graphql,");
        }
        builder.append(getPersistenceDependency(serviceName,model));
        return builder.toString();
    }

    private String getPersistenceDependency(String serviceName, Model model) {
        Persistence persistenceTemplate = getPersistence(serviceName,model);
        if(persistenceTemplate == null){
            return "";
        }
        if(persistenceTemplate.equals(Persistence.POSTGRESQL)){
            return "postgresql,";
        }
        if(persistenceTemplate.equals(Persistence.MYSQL)){
            return "mysql,";
        }
        if(persistenceTemplate.equals(Persistence.REDIS)){
            return "data-redis,cache,";
        }
        if(persistenceTemplate.equals(Persistence.KAFKA)){
            return "kafka,";
        }
        return null;
    }

    private Persistence getPersistence(String serviceName, Model model) {
        String queryStr = "SELECT ?persistence WHERE { "
                + "?service <" + BASE_URI + "serviceName> \"" + serviceName + "\" . "
                + "?service <" + BASE_URI + "persistenceType> ?persistence ."
                + "} LIMIT 1";

        try (QueryExecution qExec = QueryExecutionFactory.create(QueryFactory.create(queryStr), model)) {
            ResultSet results = qExec.execSelect();
            if (results.hasNext()) {
                String persistence = results.next().get("persistence").toString();
                for (Persistence type: Persistence.values()) {
                    if (type.name().equalsIgnoreCase(persistence)) {
                        return type;
                    }
                }
            }
        }
        return null;
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

    public List<String> getConnectedServices(String serviceName, Model model) {
        List<String> connectedServices = new ArrayList<>();
        String queryStr = "SELECT ?otherService WHERE { "
                + "{ ?connection <" + BASE_URI + "serviceOne> \"" + serviceName + "\" ; "
                + "              <" + BASE_URI + "serviceTwo> ?otherService . } "
                + " UNION "
                + "{ ?connection <" + BASE_URI + "serviceTwo> \"" + serviceName + "\" ; "
                + "              <" + BASE_URI + "serviceOne> ?otherService . } "
                + "}";

        try (QueryExecution qe = QueryExecutionFactory.create(QueryFactory.create(queryStr), model)) {
            ResultSet rs = qe.execSelect();
            while (rs.hasNext()) {
                String connectedService = rs.next().get("otherService").toString();
                connectedServices.add(connectedService);
            }
        }
        return connectedServices;
    }

    public boolean isAnyConnectedServiceNotificationService(String serviceName, Model model) {
        List<String> connectedServices = getConnectedServices(serviceName, model);
        for (String connectedService : connectedServices) {
            Template template = getTemplateType(connectedService, model);
            if (template == Template.NOTIFICATION_SERVICE_V1) {
                return true; // Si uno de los servicios conectados es de tipo Notification Service
            }
        }
        return false; // Ninguno de los servicios conectados es Notification Service
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

    private String encodeForUrl(String string){
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

    public static boolean usesGraphQL(String serviceName, Model model) {
        String queryString = """
            PREFIX j0: <http://tomasdarquier.com/tfg/>
            SELECT ?connectionType WHERE {
                ?connection j0:connectionType ?connectionType ;
                            j0:serviceOne ?serviceOne ;
                            j0:serviceTwo ?serviceTwo .
                FILTER(?serviceOne = "%s" || ?serviceTwo = "%s")
            }
        """.formatted(serviceName, serviceName);

        try (QueryExecution qe = QueryExecutionFactory.create(QueryFactory.create(queryString), model)) {
            ResultSet results = qe.execSelect();
            while (results.hasNext()) {
                QuerySolution solution = results.next();
                String connectionType = solution.getLiteral("connectionType").getString();
                if ("GraphQL".equalsIgnoreCase(connectionType)) {
                    return true;
                }
            }
        }
        return false;
    }


    // TODO, definir seguridad en servicios
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
        return "none";
    }
}
