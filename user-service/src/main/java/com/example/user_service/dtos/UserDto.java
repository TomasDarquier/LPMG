package com.example.user_service.dtos;

import com.example.user_service.entities.OauthProvider;

public record UserDto(
        OauthProvider oauthProvider,
        String oauthId,
        String email,
        String name
) {
}
