package com.example.user_service.services;

import com.example.user_service.dtos.UserDto;
import com.example.user_service.entities.User;

import java.util.Optional;

public interface UserService {

    User register(UserDto user);
    Optional<User> findById(Long id);
    Boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
}