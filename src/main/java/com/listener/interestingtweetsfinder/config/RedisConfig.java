package com.listener.interestingtweetsfinder.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfig {

    private static final int MAX_CONNECTIONS=64;
    private static final int IDLE_CONNECTIONS=64;

    @Value ("${general.redis.host}")
    private String host;

    @Value ("${general.redis.port}")
    private int port;

    @Bean
    public JedisPool jedisPool(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(MAX_CONNECTIONS);
        config.setMaxIdle(IDLE_CONNECTIONS);
        return new JedisPool(config,host,port);
    }

}
