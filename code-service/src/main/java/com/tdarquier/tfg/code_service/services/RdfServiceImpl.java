package com.tdarquier.tfg.code_service.services;

import com.tdarquier.tfg.code_service.entities.Connection;
import com.tdarquier.tfg.code_service.enums.*;
import org.apache.jena.rdf.model.RDFNode;
import org.springframework.stereotype.Service;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import com.tdarquier.tfg.code_service.enums.PersistenceType;

import java.util.*;

@Service
public class RdfServiceImpl implements RdfService{

    private static final String BASE_URI = "http://tomasdarquier.com/tfg/";

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

    public List<String> getConnectionNames(String serviceName, Model model) {
        List<String> connectionNames = new ArrayList<>();
        String queryStr = "SELECT ?connectedService WHERE { "
                + "{ ?connection <" + BASE_URI + "serviceOne> \"" + serviceName + "\" . "
                + "?connection <" + BASE_URI + "serviceTwo> ?connectedService . } "
                + "UNION "
                + "{ ?connection <" + BASE_URI + "serviceTwo> \"" + serviceName + "\" . "
                + "?connection <" + BASE_URI + "serviceOne> ?connectedService . } "
                + "}";

        try (QueryExecution qExec = QueryExecutionFactory.create(QueryFactory.create(queryStr), model)) {
            ResultSet results = qExec.execSelect();

            while (results.hasNext()) {
                QuerySolution solution = results.nextSolution();
                RDFNode connectedServiceNode = solution.get("connectedService");

                // Verificamos si connectedService es un recurso o un literal
                if (connectedServiceNode.isResource()) {
                    connectionNames.add(connectedServiceNode.asResource().getLocalName());
                } else if (connectedServiceNode.isLiteral()) {
                    connectionNames.add(connectedServiceNode.asLiteral().getString());
                }
            }
        }
        System.out.println("CONNECTION NAMES!!!!\n\n\n" + connectionNames + "\n\n");

        return connectionNames;
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
    // Obtener el puerto de un servicio
    public String getServicePort(Model model, String serviceName) {
        String queryStr = "SELECT ?port WHERE { "
                + "?service <" + BASE_URI + "serviceName> \"" + serviceName + "\" . "
                + "?service <" + BASE_URI + "port> ?port ."
                + "} LIMIT 1";

        try (QueryExecution qExec = QueryExecutionFactory.create(QueryFactory.create(queryStr), model)) {
            ResultSet results = qExec.execSelect();
            if (results.hasNext()) {
                return results.next().get("port").toString();
            }
        }
        return null;
    }

    public boolean isGatewayEnabled(Model model) {
        String queryStr = "ASK WHERE { "
                + "?project <" + BASE_URI + "hasInfrastructure> ?infrastructure . "
                + "?infrastructure <" + BASE_URI + "gatewayEnabled> \"true\" . }";

        return QueryExecutionFactory.create(QueryFactory.create(queryStr), model).execAsk();
    }

    public PersistenceType getPersistenceType(Model model, String serviceName) {
        String queryStr = "SELECT ?persistence WHERE { "
                + "?service <" + BASE_URI + "serviceName> \"" + serviceName + "\" . "
                + "?service <" + BASE_URI + "persistenceType> ?persistence ."
                + "} LIMIT 1";

        try (QueryExecution qExec = QueryExecutionFactory.create(QueryFactory.create(queryStr), model)) {
            ResultSet results = qExec.execSelect();
            if (results.hasNext()) {
                String persistence = results.next().get("persistence").toString();
                for (PersistenceType type : PersistenceType.values()) {
                    if (type.name().equalsIgnoreCase(persistence)) {
                        return type;
                    }
                }
            }
        }
        return null;
    }

    public List<Connection> getConnections(String name, Model model) {
        List<String> connectionsNames = getConnectionNames(name, model);
        if(connectionsNames == null || connectionsNames.isEmpty()) {
            return null;
        }
        List<Connection> connections = new ArrayList<>();
        connectionsNames.forEach(connectionName -> {
               connections.add(new Connection(
                       getConnectionType(connectionName, name, model),
                       getTemplateType(connectionName, model),
                       Integer.parseInt(getServicePort(model,connectionName)),
                       getApiPathPrefix(connectionName, model)
               ));
        });

        System.out.println("Conexiones pertenecientes a " + name + ": \n\n" + connections);
        return connections;
    }

    public String getApiPathPrefix(String name, Model model) {
        String queryStr = "SELECT ?pathPrefix WHERE { "
                + "?service <" + BASE_URI + "serviceName> \"" + name + "\" . "
                + "?service <" + BASE_URI + "pathPrefix> ?pathPrefix . "
                + "} LIMIT 1";

        try (QueryExecution qExec = QueryExecutionFactory.create(QueryFactory.create(queryStr), model)) {
            ResultSet results = qExec.execSelect();
            // Si se encuentran resultados, retornamos el valor de pathPrefix
            if (results.hasNext()) {
                QuerySolution solution = results.nextSolution();
                return solution.getLiteral("pathPrefix").getString();
            }
        }
        return null;
    }

    private ConnectionType getConnectionType(String serviceA, String serviceB, Model model) {
        String queryStr = "SELECT ?type WHERE { "
                + "{ ?connection <" + BASE_URI + "serviceOne> \"" + serviceA + "\" . "
                + "?connection <" + BASE_URI + "serviceTwo> \"" + serviceB + "\" . } "
                + "UNION "
                + "{ ?connection <" + BASE_URI + "serviceOne> \"" + serviceB + "\" . "
                + "?connection <" + BASE_URI + "serviceTwo> \"" + serviceA + "\" . } "
                + "?connection <" + BASE_URI + "connectionType> ?type . "  // FIX: Se corrigió el nombre de la propiedad
                + "} LIMIT 1";

        System.out.println("Ejecutando consulta SPARQL:\n" + queryStr);  // Debug

        try (QueryExecution qExec = QueryExecutionFactory.create(QueryFactory.create(queryStr), model)) {
            ResultSet results = qExec.execSelect();
            if (results.hasNext()) {
                QuerySolution solution = results.nextSolution();
                String typeValue = solution.getLiteral("type").getString();

                System.out.println("Valor obtenido: " + typeValue);  // Debug

                for (ConnectionType type : ConnectionType.values()) {
                    if (type.name().equalsIgnoreCase(typeValue)) {
                        return type;
                    }
                }
            }
        }
        return null;
    }

     public boolean usesGraphQL(String serviceName, Model model) {
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

    public boolean usesREST(String serviceName, Model model) {
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
                if ("REST".equalsIgnoreCase(connectionType)) {
                    return true;
                }
            }
        }
        return false;
    }

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
