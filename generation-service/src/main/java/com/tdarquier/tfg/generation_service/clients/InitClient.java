package com.tdarquier.tfg.generation_service.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "init-service")
@Component
public interface InitClient {

    @GetMapping("/init/poms")
    List<String> getPoms(@RequestParam String rdf);
}