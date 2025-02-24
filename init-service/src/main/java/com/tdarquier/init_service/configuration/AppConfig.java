package com.tdarquier.init_service.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@PropertySources({
        @PropertySource("classpath:service-dependencies.properties"),
        @PropertySource("classpath:redis.properties")
})
public class AppConfig {
}
