package com.tdarquier.tfg.generation_service.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tdarquier.tfg.generation_service.authentication.RequestContext;
import com.tdarquier.tfg.generation_service.clients.CodeClient;
import com.tdarquier.tfg.generation_service.clients.InitClient;
import com.tdarquier.tfg.generation_service.dtos.CodeGenerationDTO;
import com.tdarquier.tfg.generation_service.kafka.generation.GenerationRequest;
import com.tdarquier.tfg.generation_service.services.RdfService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.List;

// services-generation-request consumer
@Service
@RequiredArgsConstructor
public class SGRConsumer {

    private final RequestContext requestContext;
    RdfService rdfService;
    InitClient initClient;
    CodeClient codeClient;

    @Autowired
    public SGRConsumer(RdfService rdfService, InitClient initClient, RequestContext requestContext, CodeClient codeClient) {
        this.rdfService = rdfService;
        this.initClient = initClient;
        this.requestContext = requestContext;
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
            String projectRDF = rdfService.toRdf(record.value()).join();

            //Enviar RDF a  init-service
            List<String> poms = initClient.getPoms(projectRDF);
            poms.forEach(System.out::println);
            // TODO -- Enviar notificacion


            //Enviar poms a code-service
            CodeGenerationDTO dto = new CodeGenerationDTO(projectRDF,poms);
             codeClient.generateCode(dto, String.valueOf(record.value().userId()));
            //TODO -- Enviar notificacion

            //Recibir confirmacion de finalizacion
            //-- Enviar notificacion

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }finally {
            requestContext.clear();
        }


    }
}
