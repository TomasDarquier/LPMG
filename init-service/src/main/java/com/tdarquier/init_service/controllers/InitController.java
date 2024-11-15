package com.tdarquier.init_service.controllers;

import com.tdarquier.init_service.services.ProjectGenerationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/init")
@RequiredArgsConstructor
public class InitController {

    ProjectGenerationService projectGenerationService;

    @Autowired
    public InitController(ProjectGenerationService projectGenerationService) {
        this.projectGenerationService = projectGenerationService;
   }

    @PostMapping("/poms")
    public List<String> getPoms(@RequestBody String rdf){
        System.out.println(rdf);
        return projectGenerationService.generateProject(rdf);
    }

}
