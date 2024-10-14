package com.example.gateway.dtos;

import jakarta.validation.constraints.Email;

public record UserDto(
        OauthProvider oauthProvider,
        String oauthId,
        @Email
        String email,
        String name
) {
}
