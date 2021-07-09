package com.listener.interestingtweetsfinder.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfig {

    @Value ("${general.redis.max_connections}")
    private int max_connections=64;

    @Value ("${general.redis.idle_connections}")
    private int idle_connections=64;

    @Value ("${general.redis.host}")
    private String host;

    @Value ("${general.redis.port}")
    private int port;

    @Bean
    public JedisPool jedisPool(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(max_connections);
        config.setMaxIdle(idle_connections);
        return new JedisPool(config,host,port);
    }

}
