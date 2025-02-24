package com.tdarquier.init_service.services;

public interface RedisService {

    void set(String key, String value);
    String get(String key);
}