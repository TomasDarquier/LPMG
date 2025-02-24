package com.tdarquier.init_service.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPooled;

@Configuration
public class RedisConfig {
    @Value("${REDIS_URL}")
    private String URL;

    @Value("${REDIS_PORT}")
    private String PORT;

    @Bean
    public JedisPooled jedisClient() {
        return new JedisPooled(URL, Integer.parseInt(PORT));
    }
}