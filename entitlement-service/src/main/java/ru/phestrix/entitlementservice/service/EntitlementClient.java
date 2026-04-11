package ru.phestrix.entitlementservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import ru.phestrix.entitlementservice.model.EntitlementsProperties;

import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class EntitlementClient {
    private final RestClient restClient;
    private final EntitlementsProperties properties;

    public Map<String, Set<String>> getEntitlements(String sessionId) {
        return restClient.get()
                .uri(properties.getUrl() + "/entitlements")
                .header("X-Session-Id", sessionId)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

    }
}
