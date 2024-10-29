package com.tdarquier.init_service.services;

import com.tdarquier.init_service.clients.SpringApiClient;
import com.tdarquier.init_service.entities.ProjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

// TODO
// extraer data de RDF en metodo createProjectRequest
// crear lista de dependencias en base a config file
// retornar los poms desde el controller
// hacer un checkeo de returns de los cotrollers, usar ResopnseEntity<>

@Service
public class ProjectGenerationService {

    private final SpringApiClient springApiClient;
    private final ExecutorService executorService = Executors.newFixedThreadPool(2);

    public ProjectGenerationService(SpringApiClient springApiClient) {
        this.springApiClient = springApiClient;
    }

    public List<String> generateProject(String rdf) {
        // TODO extraer contenido del rdf
        List<String> services = new ArrayList<>();
        List<Callable<String>> tasks = new ArrayList<>();

        for(String service: services){
            ProjectRequest request = createProjectRequest(service, rdf);
            tasks.add(() -> springApiClient.generatePom(request));
        }

        // Ejecutar las tareas en paralelo y recopilar los resultados
        List<String> poms = new ArrayList<>();

        try {
            List<Future<String>> futures = executorService.invokeAll(tasks);
            for (Future<String> future : futures) {
                try {
                    poms.add(future.get()); // Obtener el POM de cada llamada
                } catch (ExecutionException e) {
                    throw new RuntimeException("Error generando POM", e);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Error ejecutando tareas en paralelo", e);
        }

        return poms;
    }

    private ProjectRequest createProjectRequest(String service, String rdf) {
        //TODO

        return null;
    }
}
