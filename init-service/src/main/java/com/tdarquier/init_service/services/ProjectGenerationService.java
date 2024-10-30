package com.tdarquier.init_service.services;

import com.tdarquier.init_service.clients.SpringApiClient;
import com.tdarquier.init_service.entities.ProjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

// TODO
//
// Agregar dependecias basicas segun el tipo de servicio
// hacer un checkeo de returns de los cotrollers, usar ResopnseEntity<>

@Service
public class ProjectGenerationService {

    private final SpringApiClient springApiClient;
    private final RdfParserService rdfParserService;
    private final ExecutorService executorService = Executors.newFixedThreadPool(2);

    // A reemplazar en un futuro por NRDB
    @Value("${CART_SERVICE_DEPENDENCIES}")
    private String cartServiceDependencies;
    @Value("${USER_SERVICE_DEPENDENCIES}")
    private String userServiceDependencies;
    @Value("${NOTIFICATION_SERVICE_DEPENDENCIES}")
    private String notificationServiceDependencies;
    @Value("${ORDERS_SERVICE_DEPENDENCIES}")
    private String orderServiceDependencies;
    @Value("${SHIPPING_SERVICE_DEPENDENCIES}")
    private String shippingServiceDependencies;
    @Value("${PRODUCTS_SERVICE_DEPENDENCIES}")
    private String productServiceDependencies;


    public ProjectGenerationService(SpringApiClient springApiClient, RdfParserService rdfParserService) {
        this.springApiClient = springApiClient;
        this.rdfParserService = rdfParserService;
    }

    public List<String> generateProject(String rdf) {
        List<ProjectRequest> services = rdfParserService.parseProjectRequest(rdf);
        services.forEach(service -> service.setDependencies(
                setBasicDependencies(service.getDependencies(), service.getArtifactId())
        ));

        List<Callable<String>> tasks = new ArrayList<>();

        for(ProjectRequest service: services){
            tasks.add(() -> springApiClient.generatePom(service));
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

    private String setBasicDependencies(String dependencies, String artifactId) {
        if(artifactId.startsWith("user-service")){
            return dependencies + userServiceDependencies;
        }
        if(artifactId.startsWith("notification-service")){
            return dependencies + notificationServiceDependencies;
        }
        if(artifactId.startsWith("cart-service")){
            return dependencies + cartServiceDependencies;
        }
        if(artifactId.startsWith("order-service")){
            return dependencies + orderServiceDependencies;
        }
        if(artifactId.startsWith("shipping-service")){
            return dependencies + shippingServiceDependencies;
        }
        if(artifactId.startsWith("product-service")){
            return dependencies + productServiceDependencies;
        }
        return dependencies.endsWith(",")?
                dependencies.substring(0, dependencies.length() - 1) :
                dependencies;
    }

}
