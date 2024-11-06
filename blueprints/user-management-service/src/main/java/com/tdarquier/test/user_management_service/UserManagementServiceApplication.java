package com.tdarquier.test.user_management_service;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class UserManagementServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(UserManagementServiceApplication.class, args);
  }

}
