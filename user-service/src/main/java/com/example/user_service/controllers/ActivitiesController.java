package com.example.user_service.controllers;


import com.example.user_service.dtos.UserDto;
import com.example.user_service.entities.User;
import com.example.user_service.services.UserActivityService;
import com.example.user_service.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/activities")
@RequiredArgsConstructor
public class ActivitiesController {

    UserService userService;
    UserActivityService userActivityService;

    @Autowired
    public ActivitiesController(UserService userService, UserActivityService userActivityService) {
        this.userService = userService;
        this.userActivityService = userActivityService;
    }

    @PostMapping("/login-actions")
    public ResponseEntity<Void> loginActions(@Valid @RequestBody UserDto user) {
        Optional<User> dbUser = userService.findByEmail(user.email());
        if(dbUser.isEmpty()) {
            User registeredUser = userService.register(user);
            userActivityService.registerSignUpActivity(registeredUser);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        userActivityService.registerLoginActivity(dbUser.get());
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
