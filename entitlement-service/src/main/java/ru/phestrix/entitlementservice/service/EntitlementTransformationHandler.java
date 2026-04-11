package ru.phestrix.entitlementservice.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class EntitlementTransformationHandler {
    private final static String CACHE_KEY_PREFIX = "entitlements:";
    private final static int CACHE_TTL_MINUTES = 30;

    private final RedisTemplate<String, Map<String, Set<String>>> redisTemplate;
    private final EntitlementClient entitlementClient;

    public Map<String, Set<String>> convertSessionIdToEntitlements(String sessionId) {
        String cacheKey = CACHE_KEY_PREFIX + sessionId;

        Map<String, Set<String>> cachedEntitlements = redisTemplate
                .opsForValue()
                .get(cacheKey);

        if (cachedEntitlements != null) {
            return cachedEntitlements;
        }

        Map<String, Set<String>> entitlements = entitlementClient
                .getEntitlements(sessionId);

        redisTemplate.opsForValue().set(
                cacheKey,
                entitlements,
                CACHE_TTL_MINUTES,
                TimeUnit.MINUTES
        );

        return entitlements;
    }
}
