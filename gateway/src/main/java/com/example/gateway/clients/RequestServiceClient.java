package com.example.gateway.clients;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "request-service")
@Component
public interface RequestServiceClient {
    @PostMapping("/generation/request/{id}")
    Boolean requestCodeGeneration(@RequestParam String json, @PathVariable Long id );
}
