package com.tdarquier.tfg.generation_service.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tdarquier.tfg.generation_service.messages.GenerationRequest;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.util.concurrent.CompletableFuture;

@Service
public class RdfServiceImpl implements RdfService {
    private final static String BASE_URI= "http://tomasdarquier.com/tfg/";

    @Async
    @Override
    public CompletableFuture<String> toRdf(GenerationRequest generationRequest) throws JsonProcessingException {
        Model model = ModelFactory.createDefaultModel();
        JsonNode rootNode = new ObjectMapper().readTree(generationRequest.servicesJson());

        // Procesamiento del proyecto
        Resource projectResource = createProjectResource(rootNode, model);
        processServices(rootNode.get("services"), projectResource, model);
        processConnections(rootNode.get("connections"), projectResource, model);
        processInfrastructure(rootNode.get("infrastructure"), projectResource, model);
        processSecurity(rootNode.get("security"), projectResource, model);

        // Escribir el RDF en formato XML
        String rdf = writeRdfToXml(model);
        //System.out.println(generationRequest.userId());

        return CompletableFuture.completedFuture(rdf);
    }

    private Resource createProjectResource(JsonNode rootNode, Model model) {
        String projectName = rootNode.get("projectName").asText().replaceAll("\\s+", "_");
        return model.createResource(BASE_URI + "project/" + projectName)
                .addProperty(model.createProperty(BASE_URI, "projectName"), rootNode.get("projectName").asText())
                .addProperty(model.createProperty(BASE_URI, "version"), rootNode.get("version").asText())
                .addProperty(model.createProperty(BASE_URI, "description"), rootNode.get("description").asText())
                .addProperty(model.createProperty(BASE_URI, "project"), rootNode.get("project").asText());
    }

    private void processServices(JsonNode servicesNode, Resource projectResource, Model model) {
        if (servicesNode != null && servicesNode.isArray()) {
            for (JsonNode service : servicesNode) {
                String serviceName = service.get("name").asText().replaceAll("\\s+", "_");
                Resource serviceResource = model.createResource(BASE_URI + "service/" + serviceName)
                        .addProperty(model.createProperty(BASE_URI, "serviceName"), service.get("name").asText())
                        .addProperty(model.createProperty(BASE_URI, "serviceDescription"), service.get("description").asText())
                        .addProperty(model.createProperty(BASE_URI, "persistenceType"), service.get("persistence").asText())
                        .addProperty(model.createProperty(BASE_URI, "springBootVersion"), service.get("sbVersion").asText())
                        .addProperty(model.createProperty(BASE_URI, "template"), service.get("template").asText())
                        .addProperty(model.createProperty(BASE_URI, "artifactId"), service.get("artifactId").asText())
                        .addProperty(model.createProperty(BASE_URI, "groupId"), service.get("groupId").asText())
                        .addProperty(model.createProperty(BASE_URI, "baseDir"), service.get("baseDir").asText())
                        .addProperty(model.createProperty(BASE_URI, "pathPrefix"), service.get("pathPrefix").asText())
                        .addProperty(model.createProperty(BASE_URI, "port"), service.get("port").asText());

                projectResource.addProperty(model.createProperty(BASE_URI, "hasService"), serviceResource);
            }
        }
    }

    private void processConnections(JsonNode connectionsNode, Resource projectResource, Model model) {
        if (connectionsNode != null && connectionsNode.isArray()) {
            for (JsonNode connection : connectionsNode) {
                String connectionUri = BASE_URI + "connection/" +
                        connection.get("serviceOne").asText().replaceAll("\\s+", "_") +
                        "_to_" + connection.get("serviceTwo").asText().replaceAll("\\s+", "_");

                System.out.println("\n\n\n CONEXION! \n\n\n" + connectionUri + "\n\n\n");

                Resource connectionResource = model.createResource(connectionUri)
                        .addProperty(model.createProperty(BASE_URI, "serviceOne"), connection.get("serviceOne").asText())
                        .addProperty(model.createProperty(BASE_URI, "serviceTwo"), connection.get("serviceTwo").asText())
                        .addProperty(model.createProperty(BASE_URI, "protocol"), connection.get("protocol").asText())
                        .addProperty(model.createProperty(BASE_URI, "connectionType"), connection.get("type").asText());
                projectResource.addProperty(model.createProperty(BASE_URI, "hasConnection"), connectionResource);
            }
        }
    }
//    private void processConnections(JsonNode connectionsNode, Resource projectResource, Model model) {
//    if (connectionsNode != null && connectionsNode.isArray()) {
//        for (JsonNode connection : connectionsNode) {
//            String serviceOneName = connection.get("serviceOne").asText().replaceAll("\\s+", "_");
//            String serviceTwoName = connection.get("serviceTwo").asText().replaceAll("\\s+", "_");
//
//            // Recuperar recursos de servicios existentes
//            Resource serviceOneResource = model.createResource(BASE_URI + "service/" + serviceOneName);
//            Resource serviceTwoResource = model.createResource(BASE_URI + "service/" + serviceTwoName);
//
//            String connectionUri = BASE_URI + "connection/" + serviceOneName + "_to_" + serviceTwoName;
//
//            Resource connectionResource = model.createResource(connectionUri)
//                    .addProperty(model.createProperty(BASE_URI, "protocol"), connection.get("protocol").asText())
//                    .addProperty(model.createProperty(BASE_URI, "connectionType"), connection.get("type").asText())
//                    // En lugar de solo agregar el nombre, referenciamos los recursos de los servicios
//                    .addProperty(model.createProperty(BASE_URI, "serviceOne"), serviceOneResource)
//                    .addProperty(model.createProperty(BASE_URI, "serviceTwo"), serviceTwoResource);
//
//            projectResource.addProperty(model.createProperty(BASE_URI, "hasConnection"), connectionResource);
//        }
//    }
//}


    private void processInfrastructure(JsonNode infrastructureNode, Resource projectResource, Model model) {
        if (infrastructureNode != null) {
            Resource infrastructureResource = model.createResource(BASE_URI + "infrastructure")
                    .addProperty(model.createProperty(BASE_URI, "configServerEnabled"), String.valueOf(infrastructureNode.get("configServer").asBoolean()))
                    .addProperty(model.createProperty(BASE_URI, "discoveryServerEnabled"), String.valueOf(infrastructureNode.get("discovery-server").asBoolean()))
                    .addProperty(model.createProperty(BASE_URI, "gatewayEnabled"), String.valueOf(infrastructureNode.get("gateway").asBoolean()));

            projectResource.addProperty(model.createProperty(BASE_URI, "hasInfrastructure"), infrastructureResource);
        }
    }

    private void processSecurity(JsonNode securityNode, Resource projectResource, Model model) {
        if (securityNode != null) {
            Resource securityResource = model.createResource(BASE_URI + "security")
                    .addProperty(model.createProperty(BASE_URI, "securityType"), securityNode.get("type").asText());

            projectResource.addProperty(model.createProperty(BASE_URI, "hasSecurity"), securityResource);
        }
    }

    private String writeRdfToXml(Model model) {
        StringWriter writer = new StringWriter();
        model.write(writer, "RDF/XML");
        return writer.toString();
    }

}
