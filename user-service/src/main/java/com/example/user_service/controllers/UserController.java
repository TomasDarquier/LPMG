package com.example.user_service.controllers;

import com.example.user_service.entities.User;
import com.example.user_service.services.UserActivityService;
import com.example.user_service.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    UserService userService;
    UserActivityService userActivityService;

    @Autowired
    public UserController(UserService userService, UserActivityService userActivityService) {
        this.userService = userService;
        this.userActivityService = userActivityService;
    }

    @GetMapping("/{id}/name")
    public ResponseEntity<String> getNameById(@PathVariable Long id) {
        Optional<String> name = userService.getNameById(id);
        return name.isEmpty() ?
                ResponseEntity.status(HttpStatus.NOT_FOUND).build() :
                ResponseEntity.ok(name.get());

    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> existsById(@PathVariable Long id) {
        return userService.findById(id).isPresent() ?
                ResponseEntity.ok(true) :
                ResponseEntity.ok(false);
    }

    @GetMapping("/getId/{email}")
    public ResponseEntity<Long> getIdByEmail(@PathVariable String email) {
        Optional<User> user = userService.findByEmail(email);
        return user.isPresent()?
                ResponseEntity.ok(user.get().getId()):
                ResponseEntity.notFound().build();
    }

}