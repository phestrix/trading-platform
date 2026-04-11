package ru.phestrix.entitlementservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.ConcurrentSessionFilter;
import ru.phestrix.entitlementservice.security.ApiAuthorizeProcessingFilter;
import ru.phestrix.entitlementservice.service.EntitlementTransformationHandler;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
@Order(1)
public class SecurityConfiguration {
    private static final String ENTITLEMENT_ROLES_DOMAIN = "entitlements";

    private final EntitlementTransformationHandler entitlementTransformationHandler;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().dispatcherTypeMatchers();
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)

                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth.requestMatchers("/v1/**").permitAll()
                        .anyRequest().authenticated())
                .addFilterAfter(
                        new ApiAuthorizeProcessingFilter(
                                entitlementTransformationHandler::convertSessionIdToEntitlements,
                                request -> {
                                    Map<String, String> headers = new HashMap<>();
                                    Enumeration<String> headerNames = request.getHeaderNames();
                                    while (headerNames.hasMoreElements()) {
                                        String name = headerNames.nextElement();
                                        headers.put(name, request.getHeader(name));
                                    }
                                    return headers;
                                },
                                Set.of(ENTITLEMENT_ROLES_DOMAIN)
                        ), ConcurrentSessionFilter.class
                )
        ;


        return http.build();
    }
}
