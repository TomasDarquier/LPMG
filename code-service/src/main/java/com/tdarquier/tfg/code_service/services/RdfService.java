package com.tdarquier.tfg.code_service.services;

import com.tdarquier.tfg.code_service.enums.*;
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

    // detecta el nombre de la conexión, si existe
    public List<String> getConnectionNames(String serviceName, Model model) {
        List<String> connectionNames = new ArrayList<>();
        String queryStr = "SELECT ?connection WHERE { "
                + "{ ?connection <" + BASE_URI + "serviceOne> \"" + serviceName + "\" . } "
                + "UNION "
                + "{ ?connection <" + BASE_URI + "serviceTwo> \"" + serviceName + "\" . } "
                + "}";

        try (QueryExecution qExec = QueryExecutionFactory.create(QueryFactory.create(queryStr), model)) {
            ResultSet results = qExec.execSelect();
            while (results.hasNext()) {
                QuerySolution solution = results.nextSolution();
                connectionNames.add(solution.getResource("connection").getLocalName()); // Agregar cada conexión a la lista
            }
        }
        return connectionNames; // Retorna la lista con todos los nombres de conexiones
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

    // Obtener tipo de persistencia
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
}
