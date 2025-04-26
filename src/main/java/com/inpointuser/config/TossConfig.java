package com.inpointuser.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

@Component
public class TossConfig {
    public static final String TOSS_API_URL = "https://api.tosspayments.com/v1/payments/";
    @Value("${toss.secret-key}")
    private String secretKey;

    @Bean
    public RestTemplate tossPaymentsRestTemplate() {
        return new RestTemplate();
    }

    public HttpHeaders getAuthHeaders() {
        HttpHeaders headers = new HttpHeaders();
        String encodedAuth = Base64.getEncoder().encodeToString((secretKey + ":").getBytes());
        headers.setBasicAuth(encodedAuth);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
