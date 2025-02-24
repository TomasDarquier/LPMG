package com.tdarquier.init_service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPooled;

@Service
public class RedisServiceImpl implements RedisService {

    final
    JedisPooled jedis;

    public RedisServiceImpl(JedisPooled jedis) {
        this.jedis = jedis;
    }

    @Override
    public void set(String key, String value) {
        jedis.set(key, value);
    }

    @Override
    public String get(String key) {
        return jedis.get(key);
    }
}