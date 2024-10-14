package com.example.gateway.clients;

import com.example.gateway.dtos.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "user-service")
@Component
public interface UserActivitiesClient {

    @PostMapping("/activities/login-actions")
    void loginActions(UserDto user);
}
