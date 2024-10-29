package com.tdarquier.init_service.clients;

import com.tdarquier.init_service.entities.ProjectRequest;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SpringApiClient {

    private final HttpClient httpClient = HttpClient.newHttpClient();

    public String generatePom(ProjectRequest projectRequest) {
        // Construir la URL para obtener solo el archivo pom.xml
        String url = "https://start.spring.io/starter.zip" +
                "?type=maven-build" +  // Solo archivo pom.xml
                "&language=java" +
                "&bootVersion=3.3.5" +
                "&groupId=" + projectRequest.groupId() +
                "&artifactId=" + projectRequest.artifactId() +
                "&name=" + projectRequest.artifactId() +
                "&description=Generated+POM" +
                "&packageName=" + projectRequest.groupId() + "." + projectRequest.artifactId() +
                "&dependencies=" + projectRequest.dependencies();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        try {
            HttpResponse<InputStream> response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
            if (response.statusCode() == 200) {
                // Convertir el InputStream a String
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.body()))) {
                    return reader.lines().collect(Collectors.joining("\n"));
                }
            } else {
                throw new RuntimeException("Failed to generate POM: HTTP " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
