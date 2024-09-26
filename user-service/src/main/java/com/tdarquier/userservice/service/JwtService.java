package com.tdarquier.userservice.service;

import com.tdarquier.userservice.model.User;

public interface JwtService {

    String generateToken(User user);

    String extractEmail(String token);

    boolean isTokenValid(String token, User user);
}