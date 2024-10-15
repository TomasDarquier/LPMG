package com.tdarquier.tfg.request_service.kafka;

import com.tdarquier.tfg.request_service.messages.GenerationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

// services-generation-request producer
@Service
@RequiredArgsConstructor
public class SGRProducer {

    private final KafkaTemplate<String, GenerationRequest> kafkaTemplate;

    public void sendGenerationRequest(GenerationRequest generationRequest){
        Message<GenerationRequest> message = MessageBuilder
                .withPayload(generationRequest)
                .setHeader(KafkaHeaders.TOPIC, "services-generation-request-topic")
                .build();

        kafkaTemplate.send(message);
    }
}
