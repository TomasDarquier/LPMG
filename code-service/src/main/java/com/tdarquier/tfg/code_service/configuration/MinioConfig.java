package com.tdarquier.tfg.code_service.configuration;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {

    @Value("${MINIO_USERNAME}")
    private String USERNAME;
    @Value("${MINIO_PASSWORD}")
    private String PASSWORD;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint("http://localhost:9000")
                .credentials(USERNAME, PASSWORD)
                .build();
    }
}
