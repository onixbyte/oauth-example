package com.onixbyte.oauth.config;

import com.onixbyte.oauth.data.PublicKeyComponent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
public class CacheConfig {

    @Bean
    public RedisSerializer<PublicKeyComponent> publicKeySerializer() {
        return new Jackson2JsonRedisSerializer<>(PublicKeyComponent.class);
    }

    @Bean
    public RedisTemplate<String, PublicKeyComponent> publicKeyCache(
            RedisConnectionFactory redisConnectionFactory,
            RedisSerializer<PublicKeyComponent> publicKeySerializer
    ) {
        var redisTemplate = new RedisTemplate<String, PublicKeyComponent>();

        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setValueSerializer(publicKeySerializer);
        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }
}
