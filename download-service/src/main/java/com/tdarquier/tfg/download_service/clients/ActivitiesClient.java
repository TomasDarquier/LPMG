package com.tdarquier.tfg.download_service.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "user-service")
@Component
public interface ActivitiesClient {
    @PostMapping("/activities/{id}/download")
    ResponseEntity<Void> registerDownloadActivity(@PathVariable Long id);
}