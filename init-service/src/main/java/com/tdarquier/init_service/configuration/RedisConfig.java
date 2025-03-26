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
        // Verifica si la URL comienza con "tcp://", y elimina el prefijo si es necesario
        String urlWithoutTcp = URL.startsWith("tcp://") ?
                URL.substring("tcp://".length()) : URL;

        // Extrae el puerto y la dirección IP
        String[] urlParts = urlWithoutTcp.split(":");
        String redisHost = urlParts[0];
        int redisPort = (urlParts.length > 1) ? Integer.parseInt(urlParts[1]) : Integer.parseInt(PORT);

        System.out.println("Redis Host: " + redisHost);
        System.out.println("Redis Port: " + redisPort);
        System.out.println("Redis URL: " + urlWithoutTcp);

        // Devuelve el cliente Jedis con la URL y el puerto correcto
        return new JedisPooled(redisHost, redisPort);
    }
}