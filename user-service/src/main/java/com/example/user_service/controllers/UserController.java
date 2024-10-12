package com.example.user_service.controllers;

import com.example.user_service.dtos.UserDto;
import com.example.user_service.entities.User;
import com.example.user_service.repositories.UserRepository;
import com.example.user_service.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/validate")
    public void registerUser(@RequestBody UserDto user) {
        userService.register(user);
    }
}