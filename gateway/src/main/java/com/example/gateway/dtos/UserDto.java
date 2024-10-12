package com.example.gateway.dtos;

public record UserDto(
        OauthProvider oauthProvider,
        String oauthId,
        String email,
        String name
) {
}
