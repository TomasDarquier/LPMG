package com.tdarquier.init_service.controllers;

import com.tdarquier.init_service.services.ProjectGenerationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/poms")
    public List<String> getPoms(@RequestParam String rdf){
        System.out.println(rdf);
        return projectGenerationService.generateProject(rdf);
    }
}
