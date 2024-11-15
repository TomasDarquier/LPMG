package com.tdarquier.tfg.code_service.services;

import com.tdarquier.tfg.code_service.entities.MinioFile;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Service
@Component
public class ProjectStructureService {

    final
    RdfService rdfService;
    final
    MinioService minioService;

    private final String gitIgnoreFile;
    private final String gitAttributesFile;

    private final String mavenWrapperFile;
    private final String mavenWrapperCMDFile;

    public ProjectStructureService(RdfService rdfService, MinioService minioService) {
        this.rdfService = rdfService;
        this.minioService = minioService;

        try {
            gitIgnoreFile = readFileFromResources(".gitignore");
            gitAttributesFile = readFileFromResources(".gitattributes");
            mavenWrapperFile = readFileFromResources("mvnw");
            mavenWrapperCMDFile = readFileFromResources("mvnw.cmd");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, String> createFolderStructure(String projectBucket, String groupId, String artifactId) {
        String formattedArtifact = artifactId.replaceAll(" ", "-");
        String formattedGroup = groupId.replaceAll(" ", "-").replaceAll("\\.", "/");
        //para los imports y carpetas no debe haber guion medio
        String artifactForFiles = formattedArtifact.replace("-", "_");

        Map<String, String> paths = new HashMap<>();

        String servicePath = formattedArtifact + "/";
        String codePath = servicePath + "src/main/java/" + formattedGroup + "/" + artifactForFiles + "/";
        String resourcesPath = servicePath + "src/main/resources/";
        String testPath = servicePath + "src/test/java/" + formattedGroup + "/" + artifactForFiles + "/";
        String groupIdFormattedToPackage = groupId.replaceAll(" ", "_") + "." + artifactForFiles;

        paths.put("service", servicePath);
        paths.put("code", codePath);
        paths.put("resources", resourcesPath);
        paths.put("test", testPath);
        paths.put("package", groupIdFormattedToPackage);

        try{
            minioService.saveObject(new MinioFile(servicePath,projectBucket ,""));
            minioService.saveObject(new MinioFile(codePath, projectBucket,""));
            minioService.saveObject(new MinioFile(resourcesPath, projectBucket,""));
            minioService.saveObject(new MinioFile(testPath, projectBucket,""));
        }catch (Exception e) {
            //todo tratar excepcion
        }
        return paths;
    }

    // git files
    public void createGitFiles(String path, String bucket){
        createGitIgnore(path, bucket);
        createGitAttributes(path, bucket);
    }
    private void createGitIgnore(String path, String bucket) {
        MinioFile gitIgnore = new MinioFile(path + ".gitignore", bucket, gitIgnoreFile);
        minioService.saveObject(gitIgnore);
    }
    private void createGitAttributes(String path, String bucket){
        MinioFile gitAttributes = new MinioFile(path + ".gitattributes", bucket, gitAttributesFile);
        minioService.saveObject(gitAttributes);
    }

    // maven files
    public void createMavenFiles(String path, String bucket){
        createMavenWrapper(path, bucket);
        createMavenWrapperCmd(path, bucket);
    }
    private void createMavenWrapper(String path, String bucket){
        MinioFile mvnw = new MinioFile(path + "mvnw", bucket, mavenWrapperFile);
        minioService.saveObject(mvnw);
    }
    private void createMavenWrapperCmd(String path, String bucket){
        MinioFile mvnwCmd = new MinioFile(path + "mvnw.cmd", bucket, mavenWrapperCMDFile);
        minioService.saveObject(mvnwCmd);
    }

    // pom.xml
    public void createPom(String path, String pom, String bucket){
        MinioFile pomXml= new MinioFile(path + "pom.xml",bucket, pom);
        minioService.saveObject(pomXml);
    }

    private String readFileFromResources(String fileName) throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/dynamic-files/" + fileName);
        if (inputStream == null) {
            throw new IOException("Archivo no encontrado: " + fileName);
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append(System.lineSeparator());
            }
            return content.toString().trim(); // Retorna el contenido sin el último salto de línea
        }
    }

}
