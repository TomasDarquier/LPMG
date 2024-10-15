package com.tdarquier.tfg.request_service.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

//user-service
@FeignClient(name = "user-service", contextId = "userData")
@Component
public interface UserClient {

    @GetMapping("/users/{id}/exists")
    Boolean existsById(@PathVariable("id") Long id);
}
