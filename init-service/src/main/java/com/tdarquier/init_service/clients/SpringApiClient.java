package com.tdarquier.init_service.clients;

import com.tdarquier.init_service.entities.ProjectRequest;
import com.tdarquier.init_service.services.RedisService;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Component
public class SpringApiClient {

    private final HttpClient httpClient = HttpClient.newHttpClient();

    private final RedisService redisService;

    public SpringApiClient(RedisService redisService) {
        this.redisService = redisService;
    }

    public String generatePom(ProjectRequest projectRequest) {
        String url = generateUrl(projectRequest);

        // si ya se hizo el request previamente se utiliza el contenido guardado en redis
        if (redisService.get(String.valueOf(url.hashCode())) != null) {
            return redisService.get(String.valueOf(url.hashCode()));
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        try {
            HttpResponse<InputStream> response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
            if (response.statusCode() == 200) {
                // Leer el archivo zip y extraer el archivo pom.xml
                try (ZipInputStream zipStream = new ZipInputStream(response.body())) {
                    ZipEntry entry;
                    while ((entry = zipStream.getNextEntry()) != null) {
                        if ("pom.xml".equals(entry.getName())) {
                            // Convertir el contenido del pom.xml a String
                            String pom = new BufferedReader(new InputStreamReader(zipStream))
                                    .lines()
                                    .collect(Collectors.joining("\n"));

                            redisService.set(
                                    String.valueOf(url.hashCode()),
                                    pom
                            );

                            return pom;
                        }
                    }
                }
                throw new RuntimeException("POM file not found in the response.");
            } else {
                throw new RuntimeException("Failed to generate POM: HTTP " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateUrl(ProjectRequest projectRequest) {
        return "https://start.spring.io/starter.zip" +
                "?type=maven-project" +  // Solicita el proyecto completo
                "&language=java" +
                "&bootVersion=3.3.5" +
                "&groupId=" + projectRequest.getGroupId() +
                "&artifactId=" + projectRequest.getArtifactId() +
                "&name=" + projectRequest.getName() +
                "&description=Generated+POM" +
                "&packageName=" + projectRequest.getGroupId() + "." + projectRequest.getArtifactId() +
                "&dependencies=" + projectRequest.getDependencies();

    }
}
