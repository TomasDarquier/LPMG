package com.tdarquier.tfg.generation_service.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tdarquier.tfg.generation_service.authentication.RequestContext;
import com.tdarquier.tfg.generation_service.clients.CodeClient;
import com.tdarquier.tfg.generation_service.clients.InitClient;
import com.tdarquier.tfg.generation_service.dtos.CodeGenerationDTO;
import com.tdarquier.tfg.generation_service.messages.GenerationRequest;
import com.tdarquier.tfg.generation_service.messages.GenerationStatus;
import com.tdarquier.tfg.generation_service.services.RdfService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

// services-generation-request consumer
@Service
@RequiredArgsConstructor
public class SGRConsumer {

    private final RequestContext requestContext;
    private final SGSProducer sgsProducer;
    RdfService rdfService;
    InitClient initClient;
    CodeClient codeClient;

    @Autowired
    public SGRConsumer(RdfService rdfService, InitClient initClient, RequestContext requestContext, SGSProducer sgsProducer, CodeClient codeClient) {
        this.rdfService = rdfService;
        this.initClient = initClient;
        this.requestContext = requestContext;
        this.sgsProducer = sgsProducer;
        this.codeClient = codeClient;
    }

    @KafkaListener(topics = "services-generation-request-topic")
    public void consumeGenerationRequest(ConsumerRecord<String, GenerationRequest> record) {

        //obtener JWT para validar peticiones con feign
        String jwt = null;
        for(Header header : record.headers()) {
            if(header.key().equals("Authorization")) {
                jwt = new String(header.value());
                break;
            }
        }
        requestContext.initialize(jwt);

        try {
            sgsProducer.sendGenerationStatus(new GenerationStatus(
                    record.value().userId(),
                    "Generando Archivos pom.xml...",
                    2,
                    new Date()
            ), jwt);

            String projectRDF = rdfService.toRdf(record.value()).join();

            //Enviar RDF a  init-service
            List<String> poms = initClient.getPoms(projectRDF);
            //poms.forEach(System.out::println);


            sgsProducer.sendGenerationStatus(new GenerationStatus(
                    record.value().userId(),
                    "Generando Microservicios...",
                    3,
                    new Date()
            ), jwt);

            //Enviar poms a code-service
            CodeGenerationDTO dto = new CodeGenerationDTO(projectRDF,poms);
            codeClient.generateCode(dto, String.valueOf(record.value().userId()));


            //TODO - Recibir confirmacion de finalizacion
            sgsProducer.sendGenerationStatus(new GenerationStatus(
                    record.value().userId(),
                    "Arquitectura Generada con Exito",
                    4,
                    new Date()
            ), jwt);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }finally {
            requestContext.clear();
        }


    }
}