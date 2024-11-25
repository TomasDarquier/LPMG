package com.tdarquier.init_service.services;

import com.tdarquier.init_service.clients.SpringApiClient;
import com.tdarquier.init_service.entities.ProjectRequest;
import com.tdarquier.init_service.enums.Template;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
public class ProjectGenerationServiceImpl implements ProjectGenerationService{

    private static final Map<String, String> SERVICE_DEPENDENCIES = Map.of(
            "USER_SERVICE_V1", "userServiceDependencies",
            "NOTIFICATION_SERVICE_V1", "notificationServiceDependencies",
            "CART_SERVICE_V1", "cartServiceDependencies",
            "ORDER_SERVICE_V1", "orderServiceDependencies",
            "SHIPPING_SERVICE_V1", "shippingServiceDependencies",
            "PRODUCT_SERVICE_V1", "productServiceDependencies"
    );

    private final SpringApiClient springApiClient;
    private final RdfParserService rdfParserService;
    private final NonInitializrDependenciesServiceImpl nonInitializrDependenciesService;
    private final ExecutorService executorService = Executors.newFixedThreadPool(2);

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
    @Value("${PRODUCT_SERVICE_DEPENDENCIES}")
    private String productServiceDependencies;

    public ProjectGenerationServiceImpl(SpringApiClient springApiClient, RdfParserServiceImpl rdfParserService, NonInitializrDependenciesServiceImpl nonInitializrDependenciesService) {
        this.springApiClient = springApiClient;
        this.rdfParserService = rdfParserService;
        this.nonInitializrDependenciesService = nonInitializrDependenciesService;
    }

    public List<String> generateProject(String rdf) {
        List<ProjectRequest> components = rdfParserService.parseProjectRequest(rdf);

        // Añadir dependencias básicas
        components.forEach(component -> component.setDependencies(
                setBasicDependencies(component.getDependencies(), component.getTemplate())
        ));

        // Crear tareas para cada componente
        List<Callable<String>> tasks = components.stream()
                .map(component -> (Callable<String>) () -> springApiClient.generatePom(component))
                .collect(Collectors.toList());

        // Ejecutar las tareas en paralelo y recopilar los resultados
        List<String> poms = executeTasksInParallel(tasks);

        // Insertar dependencias no presentes en Initializr API
        return poms.stream()
                .map(pom -> processPom(pom, rdf))
                .collect(Collectors.toList());
    }

    private List<String> executeTasksInParallel(List<Callable<String>> tasks) {
        try {
            List<Future<String>> futures = executorService.invokeAll(tasks);
            List<String> results = new ArrayList<>();
            for (Future<String> future : futures) {
                try {
                    results.add(future.get());
                } catch (ExecutionException e) {
                    throw new RuntimeException("Error generando POM", e);
                }
            }
            return results;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Error ejecutando tareas en paralelo", e);
        }
    }

    private String setBasicDependencies(String dependencies, Template template) {
        return Optional.ofNullable(SERVICE_DEPENDENCIES.get(template.toString()))
                .map(dep -> dependencies + getDependencyByName(dep))
                .orElse(dependencies.endsWith(",") ? dependencies.substring(0, dependencies.length() - 1) : dependencies);
    }

    private String getDependencyByName(String dependencyName) {
        return switch (dependencyName) {
            case "userServiceDependencies" ->
                    userServiceDependencies;
            case "notificationServiceDependencies" ->
                    notificationServiceDependencies;
            case "cartServiceDependencies" ->
                    cartServiceDependencies;
            case "orderServiceDependencies" ->
                    orderServiceDependencies;
            case "shippingServiceDependencies" ->
                    shippingServiceDependencies;
            case "productServiceDependencies" ->
                    productServiceDependencies;
            default ->
                    "";
        };
    }

    private String processPom(String pom, String rdf) {
        Model model = ModelFactory.createDefaultModel();
        model.read(new StringReader(rdf), null, "RDF/XML");
        Template template = rdfParserService.getTemplateType(getNameByPom(pom), model);
        List<String> extraDependencies = nonInitializrDependenciesService.getDependenciesByTemplate(template);
        return nonInitializrDependenciesService.getCompletePom(pom, extraDependencies);
    }

    private String getNameByPom(String pom) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputStream inputStream = new ByteArrayInputStream(pom.getBytes(StandardCharsets.UTF_8));
            Document document = builder.parse(inputStream);
            return document.getElementsByTagName("name").item(0).getTextContent();
        } catch (Exception e) {
            return null;
        }
    }
}