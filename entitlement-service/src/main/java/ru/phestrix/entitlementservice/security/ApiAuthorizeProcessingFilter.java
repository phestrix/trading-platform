package ru.phestrix.entitlementservice.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

@RequiredArgsConstructor
public class ApiAuthorizeProcessingFilter extends OncePerRequestFilter {
    private static final String SESSION_ID_HEADER = "X-Session-Id";
    private static final String ENTITLEMENTS_HEADER = "X-Entitlements";
    private final Function<String, Map<String, Set<String>>> entitlementProvider;
    private final Function<HttpServletRequest, Map<String, String>> headersExtractor;
    private final Set<String> allowedRoles;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            String sessionId = extractAndValidateSessionId(request);

            Map<String, Set<String>> entitlements = entitlementProvider.apply(sessionId);

            enrichResponseWithEntitlements(response, entitlements);

            filterChain.doFilter(request, response);

        } catch (SecurityException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

    }

    private String extractAndValidateSessionId(HttpServletRequest request) {
        String sessionId = request.getHeader(SESSION_ID_HEADER);
        if (sessionId == null || sessionId.trim().isEmpty()) {
            throw new SecurityException("Missing or invalid session ID");
        }
        return sessionId;
    }

    private void enrichResponseWithEntitlements(HttpServletResponse response, Map<String, Set<String>> entitlements) throws IOException {
        String entitlementsJson = new ObjectMapper().writeValueAsString(entitlements);
        response.setHeader(ENTITLEMENTS_HEADER, entitlementsJson);
        response.setHeader("X-Content-Type-Options", "nosniff");
        response.setHeader("X-Frame-Options", "DENY");
        response.setHeader("X-XSS-Protection", "1; mode=block");
    }
}
