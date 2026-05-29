package com.teamcrocodile.tripwire.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.teamcrocodile.tripwire.client.dto.DymoRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class DymoClient {

    private final RestTemplate restTemplate;
    private final String verifyUrl;
    private final String apiToken;

    public DymoClient(
            @Value("${dymo.api.verify-url:https://api.tpeoficial.com/v1/private/secure/verify}") String verifyUrl,
            @Value("${dymo.api.token:}") String apiToken) {
        this.restTemplate = new RestTemplate();
        this.verifyUrl = verifyUrl;
        this.apiToken = apiToken;
    }

    public JsonNode verify(DymoRequest request) {
        if (!StringUtils.hasText(apiToken)) {
            return null;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiToken);

        try {
            return restTemplate.postForObject(verifyUrl, new HttpEntity<>(request, headers), JsonNode.class);
        } catch (RestClientException ex) {
            return null;
        }
    }
}
