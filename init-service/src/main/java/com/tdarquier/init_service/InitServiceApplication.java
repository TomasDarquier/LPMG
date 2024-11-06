package com.tdarquier.init_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class InitServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InitServiceApplication.class, args);
	}

	// TODO agregar dependencias NO presentes en initializr (si o si agregadas manualmente)
	// TODO agregar dependencias segun BD (metodo a implementar)
}
