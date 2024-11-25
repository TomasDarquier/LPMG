package com.example.user_service.controllers;

import com.example.user_service.dtos.UserDto;
import com.example.user_service.entities.User;
import com.example.user_service.services.UserActivityService;
import com.example.user_service.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/activities")
@RequiredArgsConstructor
public class ActivitiesController {

    private final UserService userService;
    private final UserActivityService userActivityService;

    @PostMapping("/access")
    public ResponseEntity<Void> registerAccessActivities(@Valid @RequestBody UserDto user) {
        Optional<User> dbUser = userService.findByEmail(user.email());
        if(dbUser.isEmpty()) {
            User registeredUser = userService.register(user);
            userActivityService.registerSignUpActivity(registeredUser);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        userActivityService.registerLoginActivity(dbUser.get());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/{id}/generate")
    public ResponseEntity<Void> registerCodeGenerationActivity(@PathVariable Long id) {
        Optional<User> dbUser = userService.findById(id);
        if(dbUser.isPresent()) {
            userActivityService.registerCodeGenerationActivity(dbUser.get());
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping("/{id}/download")
    public ResponseEntity<Void> registerDownloadActivity(@PathVariable Long id) {
        Optional<User> dbUser = userService.findById(id);
        if(dbUser.isPresent()) {
            userActivityService.registerDownloadActivity(dbUser.get());
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
