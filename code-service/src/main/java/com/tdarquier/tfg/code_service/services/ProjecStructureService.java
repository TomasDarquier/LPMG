package com.tdarquier.tfg.code_service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjecStructureService {

    RdfService rdfService;

    public ProjecStructureService(RdfService rdfService) {
        this.rdfService = rdfService;
    }

    public void createFolderStructure(){

    }

    // git files
    private void createGitFiles(){

    }
    private void createGitIgnore(){

    }
    private void createGitAttributes(){

    }

    // maven files
    private void createMavenFiles(){

    }
    private void createMavenWrapper(){

    }
    private void createMavenWrapperCmd(){

    }

    // pom.xml
    private void createPom(){

    }

}
