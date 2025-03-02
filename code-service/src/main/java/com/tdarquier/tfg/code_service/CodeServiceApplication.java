package com.tdarquier.tfg.code_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class CodeServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CodeServiceApplication.class, args);
	}

}
