package com.tdarquier.tfg.request_service.kafka;

import com.tdarquier.tfg.request_service.messages.GenerationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SGSProducer {

    private final KafkaTemplate<String, GenerationStatus> kafkaTemplate;

    public void sendGenerationStatus(GenerationStatus generationStatus, String jwt){
        Message<GenerationStatus> message = MessageBuilder
                .withPayload(generationStatus)
                .setHeader(KafkaHeaders.TOPIC, "services-generation-status-topic")
                .setHeader("Authorization", jwt)
                .build();

        kafkaTemplate.send(message);
    }
}
