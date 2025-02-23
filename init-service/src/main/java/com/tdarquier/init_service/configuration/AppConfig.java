package com.tdarquier.init_service.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:service-dependencies.properties")
public class AppConfig {
}
