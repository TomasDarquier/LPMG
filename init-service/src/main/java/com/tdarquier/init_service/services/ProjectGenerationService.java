package com.tdarquier.init_service.services;

import com.tdarquier.init_service.clients.SpringApiClient;
import com.tdarquier.init_service.entities.ProjectRequest;
import com.tdarquier.init_service.enums.Template;
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
        List<ProjectRequest> components = rdfParserService.parseProjectRequest(rdf);
        //agregar dependencias presentes en initializr
        components.forEach(component -> component.setDependencies(
                setBasicDependencies(component.getDependencies(), component.getTemplate())
        ));

        List<Callable<String>> tasks = new ArrayList<>();

        for(ProjectRequest component: components){
            tasks.add(() -> springApiClient.generatePom(component));
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

    private String setBasicDependencies(String dependencies, Template template) {
        if(template.toString().equalsIgnoreCase("USER_SERVICE_V1")){
            return dependencies + userServiceDependencies;
        }
        if(template.toString().equalsIgnoreCase("NOTIFICATION_SERVICE_V1")){
            return dependencies + notificationServiceDependencies;
        }
        if(template.toString().equalsIgnoreCase("CART_SERVICE_V1")){
            return dependencies + cartServiceDependencies;
        }
        if(template.toString().equalsIgnoreCase("ORDER_SERVICE_V1")){
            return dependencies + orderServiceDependencies;
        }
        if(template.toString().equalsIgnoreCase("SHIPPING_SERVICE_V1")){
            return dependencies + shippingServiceDependencies;
        }
        if(template.toString().equalsIgnoreCase("PRODUCTS_SERVICE_V1")){
            return dependencies + productServiceDependencies;
        }
        return dependencies.endsWith(",")?
                dependencies.substring(0, dependencies.length() - 1) :
                dependencies;
    }

}
