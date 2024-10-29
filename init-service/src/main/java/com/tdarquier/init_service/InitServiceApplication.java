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

}
