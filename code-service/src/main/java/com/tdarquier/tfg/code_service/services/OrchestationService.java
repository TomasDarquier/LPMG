package com.tdarquier.tfg.code_service.services;

import com.tdarquier.tfg.code_service.entities.ComponentData;
import com.tdarquier.tfg.code_service.entities.Connection;
import com.tdarquier.tfg.code_service.enums.Template;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class OrchestationService {

    MinioService minioService;
    RdfService rdfService;
    DynamicCodeGenerationService codeGenerationService;
    ProjectStructureService projectStructureService;

    private static final String PROJECTS_BUCKET = "projects";

    public OrchestationService(MinioService minioService, RdfService rdfService, DynamicCodeGenerationService codeGenerationService, ProjectStructureService projecStructureService) {
        this.minioService = minioService;
        this.rdfService = rdfService;
        this.codeGenerationService = codeGenerationService;
        this.projectStructureService = projecStructureService;
    }

    public void createArchitectureCode(List<String> poms, String rdfModel, Long userId){

        Model model = ModelFactory.createDefaultModel();
        model.read(new java.io.StringReader(rdfModel), null, "RDF/XML");
        boolean isConfigServerEnabled = rdfService.isConfigServerEnabled(model);

        // creacion del bucket que contendra toda la arquitectura
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss.SSS");
        String formattedTimestamp = now.format(formatter);
        String projectBucket = userId + "-" + formattedTimestamp;
        try{
            minioService.createNewBucket(projectBucket);
        }catch (Exception e){
            //todo tratar excepcion
        }

        // creacion de cada componente
        poms.forEach(pom -> {

            String name = getNameByPom(pom);
            Map<String, String> serviceIds = new HashMap<>();

            // TODO
            // PARA RESOLVER ESTO SE PODRIA AGREGAR AL RDF LO CORRESPONDIENTE PARA QUE SE CREEN LOS COMPONENTES!
            if(name.equalsIgnoreCase("config-server")){
                serviceIds.put("groupId","com.config.server");
                serviceIds.put("artifactId","config-server");
            } else if (name.equalsIgnoreCase("gateway")) {
                serviceIds.put("groupId","com.api.gateway");
                serviceIds.put("artifactId","api-gateway");
            } else if (name.equalsIgnoreCase("discovery-server")) {
                serviceIds.put("groupId","com.discovery.server");
                serviceIds.put("artifactId","discovery-server");
            } else{
                serviceIds = rdfService.getArtifactAndGroupIds(name, model);
            }

            // se crean todas las carpetas basicas de un projecto Spring y se retornan sus paths (service, test, resources, code)
            Map<String, String> servicePaths = projectStructureService.createFolderStructure(
                    projectBucket,
                    serviceIds.get("groupId"),
                    serviceIds.get("artifactId")
            );

            projectStructureService.createGitFiles(servicePaths.get("service"), projectBucket);
            projectStructureService.createMavenFiles(servicePaths.get("service"), projectBucket);
            projectStructureService.createPom(servicePaths.get("service"), pom, projectBucket);

            // se genera el codigo
            ComponentData componentData;
            if(isUtilService(name)){
                componentData = new ComponentData(
                        getUtilServiceTemplate(name),
                        name,
                        null,
                        servicePaths,
                        null,
                        null,
                        isConfigServerEnabled,
                        null
                );
            }else{
                List<Connection> connections = rdfService.isPartOfConnection(name,model) ?
                        rdfService.getConnections(name,model):
                        null;
                componentData = new ComponentData(
                        rdfService.getTemplateType(name,model),
                        name,
                        connections,
                        servicePaths,
                        Integer.parseInt(rdfService.getServicePort(model,name)),
                        rdfService.getApiPathPrefix(name,model),
                        isConfigServerEnabled,
                        rdfService.getPersistenceType(model, name)
                );
            }

            System.out.println("Component data: \n\n" + componentData);
            codeGenerationService.generateServiceCode(componentData, projectBucket);
        });
    }

    private Template getUtilServiceTemplate(String name) {
        if(name.equalsIgnoreCase("config-server")){
            return Template.CONFIGURATION_SERVICE_V1;
        }
        if(name.equalsIgnoreCase("gateway")){
            return Template.GATEWAY_SERVICE_V1;
        }
        if(name.equalsIgnoreCase("discovery-server")){
            return Template.DISCOVERY_SERVICE_V1;
        }
        return null;
    }

    private boolean isUtilService(String name) {
        return name.equalsIgnoreCase("config-server")||
                        name.equalsIgnoreCase("gateway")||
                        name.equalsIgnoreCase("discovery-server");
    }

    private String getNameByPom(String pom) {
        String regex = "<name>(.*?)</name>";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(pom);

        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}
