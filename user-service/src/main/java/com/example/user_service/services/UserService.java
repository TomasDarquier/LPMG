package com.example.user_service.services;

import com.example.user_service.dtos.UserDto;
import com.example.user_service.entities.User;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User register(UserDto user);
    Optional<User> findById(Long id);
    Boolean existsByEmail(String email);
}