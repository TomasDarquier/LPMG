package com.example.user_service;

import com.example.user_service.entities.User;
import com.example.user_service.entities.UserActivity;
import com.example.user_service.entities.UserSession;
import com.example.user_service.repositories.UserActivityRepository;
import com.example.user_service.repositories.UserRepository;
import com.example.user_service.repositories.UserSessionRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.stream.Stream;

@SpringBootApplication
public class UserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

}
