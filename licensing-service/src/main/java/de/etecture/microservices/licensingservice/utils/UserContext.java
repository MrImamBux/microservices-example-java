package de.etecture.microservices.licensingservice.utils;

import org.springframework.stereotype.Component;

@Component
public class UserContext {

    public static final String ETECTURE_CORRELATION_ID = "etecture-correlation-id";
    public static final String AUTH_TOKEN = "Authorization";

    private String correlationId;
    private String authToken;

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
