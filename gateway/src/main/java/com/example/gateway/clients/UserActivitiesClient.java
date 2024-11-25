package com.example.gateway.clients;

import com.example.gateway.dtos.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "user-service", contextId = "userActivities")
@Component
public interface UserActivitiesClient {

    @PostMapping("/activities/access")
    void registerAccessActivities(UserDto user);
}
