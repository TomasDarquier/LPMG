package com.tdarquier.tfg.generation_service.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tdarquier.tfg.generation_service.kafka.generation.GenerationRequest;
import com.tdarquier.tfg.generation_service.services.RdfService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

// services-generation-request consumer
@Service
@RequiredArgsConstructor
public class SGRConsumer {

    RdfService rdfService;

    @Autowired
    public SGRConsumer(RdfService rdfService) {
        this.rdfService = rdfService;
    }

    @KafkaListener(topics = "services-generation-request-topic")
    public void consumeGenerationRequest(GenerationRequest generationRequest) {
        //transformar JSON a rdf
        try {
            String projectRDF = String.valueOf(rdfService.toRdf(generationRequest));
        } catch (JsonProcessingException e) {
            System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa PROBLEMON");
            throw new RuntimeException(e);
        }
        //Enviar RDF a  init-service
        // String projectPoms = initClient.getPoms(projectRDF);
        // -- Enviar notificacion
        //Enviar poms a code-service
        // codeClient.generateCode(projectRDF, projectPoms);
        // -- Enviar notificacion
        //Recibir confirmacion de finalizacion
        // -- Enviar notificacion
    }
}
