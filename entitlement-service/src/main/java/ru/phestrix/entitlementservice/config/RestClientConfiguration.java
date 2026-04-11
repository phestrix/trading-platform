package ru.phestrix.entitlementservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfiguration {
    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                .baseUrl("/api/v1")
                .build();
    }
}
