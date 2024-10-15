package com.tdarquier.tfg.request_service.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

// SGR = services-generation-request
@Configuration
public class KafkaSGRTopicConfiguration {

    @Bean
    public NewTopic servicesGenerationRequestTopic() {
        return TopicBuilder
                .name("services-generation-request-topic")
                .build();
    }
}
