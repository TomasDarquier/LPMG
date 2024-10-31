package com.tdarquier.tfg.code_service.services;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class OrchestationService {

    MinioService minioService;
    RdfService rdfService;
    DinamicCodeGenerationService codeGenerationService;
    ProjectStructureService projectStructureService;

    private static final String PROJECTS_BUCKET = "projects";

    public OrchestationService(MinioService minioService, RdfService rdfService, DinamicCodeGenerationService codeGenerationService, ProjectStructureService projecStructureService) {
        this.minioService = minioService;
        this.rdfService = rdfService;
        this.codeGenerationService = codeGenerationService;
        this.projectStructureService = projecStructureService;
    }

    public void createArchitectureCode(List<String> poms, String rdfModel, Long userId){

        Model model = ModelFactory.createDefaultModel();
        model.read(new java.io.StringReader(rdfModel), null, "RDF/XML");

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

        });

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
