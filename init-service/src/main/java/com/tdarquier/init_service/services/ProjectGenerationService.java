package com.tdarquier.init_service.services;

import com.tdarquier.init_service.clients.SpringApiClient;
import com.tdarquier.init_service.entities.ProjectRequest;
import com.tdarquier.init_service.enums.Template;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class ProjectGenerationService {

    private final SpringApiClient springApiClient;
    private final RdfParserService rdfParserService;
    private final NonInitializrDependenciesService nonInitializrDependenciesService;
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


    public ProjectGenerationService(SpringApiClient springApiClient, RdfParserService rdfParserService, NonInitializrDependenciesService nonInitializrDependenciesService) {
        this.springApiClient = springApiClient;
        this.rdfParserService = rdfParserService;
        this.nonInitializrDependenciesService = nonInitializrDependenciesService;
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

        // Insertar dependencias no presentes en initializr API
        List<String> completePoms = new ArrayList<>();
        poms.forEach(pom -> {
            Model model = ModelFactory.createDefaultModel();
            model.read(new java.io.StringReader(rdf), null, "RDF/XML");
            Template template = rdfParserService.getTemplateType(getNameByPom(pom), model);
            List<String> extraDependencies = nonInitializrDependenciesService.getDependenciesByTemplate(template);
            completePoms.add(nonInitializrDependenciesService.getCompletePom(pom,extraDependencies));
        });

        return completePoms;
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
