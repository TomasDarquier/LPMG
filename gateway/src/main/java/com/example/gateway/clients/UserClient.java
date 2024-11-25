package com.example.gateway.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", contextId = "userData")
@Component
public interface UserClient {

    @GetMapping("/users/getId/{email}")
    Long getIdByEmail(@PathVariable String email);
}
