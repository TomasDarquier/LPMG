package com.example.gateway.clients;

import com.example.gateway.dtos.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "user-service")
@Component
public interface UserClient {

    @PostMapping("/activities/access")
    void accessActivities(UserDto user);

    @GetMapping("/users/getId/{email}")
    Long getIdByEmail(@PathVariable String email);
}
