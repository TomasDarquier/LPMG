package com.tdarquier.tfg.request_service.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

// SGS service-generation-status
@Configuration
public class KafkaSGSTopicConfiguration {

    @Bean
    public NewTopic servicesGenerationStatusTopic() {
        return TopicBuilder
                .name("services-generation-status-topic")
                .build();
    }
}
