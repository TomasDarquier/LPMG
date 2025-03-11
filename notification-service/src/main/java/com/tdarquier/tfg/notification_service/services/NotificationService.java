package com.tdarquier.tfg.notification_service.services;

import com.tdarquier.tfg.notification_service.messages.GenerationStatus;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final SimpMessagingTemplate template;

    public NotificationService(SimpMessagingTemplate template) {
        this.template = template;
    }

    @KafkaListener(topics = "services-generation-status-topic")
    public void consumeGenerationStatus(ConsumerRecord<String, GenerationStatus> record) {
        GenerationStatus status = record.value();
        template.convertAndSend("/topic/generation-status/" + status.userId(), status);
    }
}