package com.tdarquier.userservice.service;

import com.tdarquier.userservice.model.User;

import java.util.Optional;

public interface UserService {

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

    Optional<User> save(User user);
}