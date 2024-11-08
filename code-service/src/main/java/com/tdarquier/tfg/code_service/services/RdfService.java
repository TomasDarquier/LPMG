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
public class RdfService {

    //Necesito saber
    //
    // Que TIPO de servicio es
    //
    //Tiene relacion?
    // cual?(de que tipo es la relacion)
    //
    //Data (artifact,etc)
    //
    //Puerto (application.properties)
    //
    //Persistencia (application.properties)
    //
    //Persistencia (Docker compose)
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

    // verifica si un servicio específico es parte de una conexión
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

        System.out.println("Conexiones pertenecientes a " + name + ": \n\n" );
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
        // Retornamos null si no se encontró pathPrefix para el servicio dado
        return null;
    }

    private ConnectionType getConnectionType(String serviceA, String serviceB, Model model) {
        String queryStr = "SELECT ?type WHERE { "
                + "{ ?connection <" + BASE_URI + "serviceOne> \"" + serviceA + "\" . "
                + "?connection <" + BASE_URI + "serviceTwo> \"" + serviceB + "\" . } "
                + "UNION "
                + "{ ?connection <" + BASE_URI + "serviceOne> \"" + serviceB + "\" . "
                + "?connection <" + BASE_URI + "serviceTwo> \"" + serviceA + "\" . } "
                + "?connection <" + BASE_URI + "type> ?type . "
                + "} LIMIT 1";

        try (QueryExecution qExec = QueryExecutionFactory.create(QueryFactory.create(queryStr), model)) {
            ResultSet results = qExec.execSelect();
            // Si se encuentra la conexión, obtenemos el tipo de conexión y validamos si coincide con ConnectionType
            if (results.hasNext()) {
                QuerySolution solution = results.nextSolution();
                String typeValue = solution.getLiteral("type").getString();

                // Verificamos si el valor obtenido coincide con alguno de los valores del enum ConnectionType
                for (ConnectionType type : ConnectionType.values()) {
                    if (type.name().equalsIgnoreCase(typeValue)) {
                        return type;
                    }
                }
            }
        }
        // Retornamos null si no se encontró el tipo de conexión entre los servicios dados
        return null;
    }

}
