package ru.phestrix.entitlementservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.phestrix.entitlementservice.model.EntitlementsProperties;

@SpringBootApplication
@EnableConfigurationProperties(EntitlementsProperties.class)
public class EntitlementServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EntitlementServiceApplication.class, args);
    }

}
