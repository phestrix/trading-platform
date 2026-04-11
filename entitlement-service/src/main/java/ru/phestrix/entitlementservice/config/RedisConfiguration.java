package ru.phestrix.entitlementservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.Set;

@Configuration
public class RedisConfiguration {

    @Bean
    public RedisTemplate<String, Map<String, Set<String>>> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Map<String, Set<String>>> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJacksonJsonRedisSerializer(new ObjectMapper()));
        return template;
    }
}
