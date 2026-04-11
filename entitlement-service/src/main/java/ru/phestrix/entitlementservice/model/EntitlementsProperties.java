package ru.phestrix.entitlementservice.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "entitlements")
@RequiredArgsConstructor
@Getter
@Setter
public class EntitlementsProperties {
    private String url;
    private int connectionTimeout = 5000;
    private int readTimeout = 5000;
}
