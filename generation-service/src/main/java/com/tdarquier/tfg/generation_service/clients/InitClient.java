package com.tdarquier.tfg.generation_service.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "init-service")
@Component
public interface InitClient {

    @PostMapping("/init/poms")
    List<String> getPoms(@RequestBody String rdf);
}
