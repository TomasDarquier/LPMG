package com.tdarquier.init_service.services;

import com.tdarquier.init_service.enums.Template;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class NonInitializrDependenciesServiceImpl implements NonInitializrDependenciesService{

    @Value("${NON_INITIALIZR_CART_SERVICE_DEPENDENCIES}")
    private String cartServiceDependencies;
    @Value("${NON_INITIALIZR_USER_SERVICE_DEPENDENCIES}")
    private String userServiceDependencies;
    @Value("${NON_INITIALIZR_NOTIFICATION_SERVICE_DEPENDENCIES}")
    private String notificationServiceDependencies;
    @Value("${NON_INITIALIZR_ORDERS_SERVICE_DEPENDENCIES}")
    private String orderServiceDependencies;
    @Value("${NON_INITIALIZR_SHIPPING_SERVICE_DEPENDENCIES}")
    private String shippingServiceDependencies;
    @Value("${NON_INITIALIZR_PRODUCTS_SERVICE_DEPENDENCIES}")
    private String productServiceDependencies;


    public List<String> getDependenciesByTemplate(Template template) {
        try{
            if(template.toString().equalsIgnoreCase("USER_SERVICE_V1")) {
                return List.of(userServiceDependencies.split(","));
            }
            if(template.toString().equalsIgnoreCase("CART_SERVICE_V1")) {
                return List.of(cartServiceDependencies.split(","));
            }
            if(template.toString().equalsIgnoreCase("SHIPPING_SERVICE_V1")) {
                return List.of(shippingServiceDependencies.split(","));
            }
            if(template.toString().equalsIgnoreCase("ORDER_SERVICE_V1")) {
                return List.of(orderServiceDependencies.split(","));
            }
            if(template.toString().equalsIgnoreCase("PRODUCT_SERVICE_V1")) {
                return List.of(productServiceDependencies.split(","));
            }
            if(template.toString().equalsIgnoreCase("NOTIFICATION_SERVICE_V1")) {
                return List.of(notificationServiceDependencies.split(","));
            }
        }catch (Exception e){
            return new ArrayList<>();
        }
        return new ArrayList<>();
    }

    public String getCompletePom(String pom, List<String> extraDependencies) {
        if(extraDependencies.isEmpty()) {
            return pom;
        }
        StringBuilder pomSb = new StringBuilder(pom);
        StringBuilder newDependencies = new StringBuilder("\n");
        extraDependencies.forEach(dependency -> {
            try {
                newDependencies.append(readDependencyFromResources(dependency).concat("\n"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        int pointToInsertDependencies = pom.indexOf("</dependencies>");
        pomSb.insert(pointToInsertDependencies, newDependencies.toString());
        return pomSb.toString();
    }

    private String readDependencyFromResources(String fileName) throws IOException {
        // asi se evita, en caso de recibir un string vacio, que el metodo retorne un 'ls' de la carpeta /non-initializr-dependenices
        if(fileName == null || fileName.isEmpty()) {
            return "";
        }
        InputStream inputStream = getClass().getResourceAsStream("/non-initializr-dependencies/" + fileName);
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
