package com.example.gateway.clients;

import com.example.gateway.dtos.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "user-service")
@Component
public interface UserClient {

    @PostMapping("/users/validate")
    void registerUser(UserDto user);
}
