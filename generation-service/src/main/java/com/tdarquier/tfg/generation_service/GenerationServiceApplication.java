package com.tdarquier.tfg.generation_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class GenerationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GenerationServiceApplication.class, args);
	}

}
