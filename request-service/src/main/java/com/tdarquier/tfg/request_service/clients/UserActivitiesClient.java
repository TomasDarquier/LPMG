package com.tdarquier.tfg.request_service.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "user-service", contextId = "userActivities")
@Component
public interface UserActivitiesClient {

    @PostMapping("/activities/{id}/generate")
    public ResponseEntity<Void> CodeGenerationActivity(@PathVariable("id") Long userId);
}
