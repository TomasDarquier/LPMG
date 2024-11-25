package com.tdarquier.init_service.controllers;

import com.tdarquier.init_service.services.ProjectGenerationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/init")
@RequiredArgsConstructor
public class InitController {

    private final ProjectGenerationServiceImpl projectGenerationService;

    @PostMapping("/poms")
    public List<String> getPoms(@RequestBody String rdf){
        System.out.println(rdf);
        return projectGenerationService.generateProject(rdf);
    }

}
