package com.teamcrocodile.tripwire.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamcrocodile.tripwire.client.dto.DymoRequest;
import com.teamcrocodile.tripwire.client.dto.DymoResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

/**
 * HTTP client for the Dymo external fraud/risk scoring API.
 * Sends account details (email, phone, IP, IBAN) to the Dymo verify endpoint
 * and returns a typed DymoResponse containing a risk score and fraud signals.
 *
 * Configuration (application.properties):
 *   dymo.api.token       — your Dymo API bearer token (required)
 *   dymo.api.verify-url  — optional override for the API endpoint URL
 */
@Component
public class DymoClient {

    // Used to make the HTTP POST request to the Dymo API
    private final RestTemplate restTemplate;

    // Used to convert the raw JSON response into a typed DymoResponse object
    private final ObjectMapper objectMapper;

    // The Dymo API endpoint URL — read from application.properties,
    // defaults to the standard Dymo verify endpoint if not set
    private final String verifyUrl;

    // Bearer token for authenticating with the Dymo API — read from application.properties
    // If empty or not set, all API calls are skipped
    private final String apiToken;

    public DymoClient(
            ObjectMapper objectMapper,
            @Value("${dymo.api.verify-url:https://api.tpeoficial.com/v1/private/secure/verify}") String verifyUrl,
            @Value("${dymo.api.token:}") String apiToken) {
        this.restTemplate = new RestTemplate();
        this.objectMapper = objectMapper;
        this.verifyUrl = verifyUrl;
        this.apiToken = apiToken;
    }

    /**
     * Sends account data to the Dymo API and returns a risk/fraud scoring response.
     *
     * @param request  account details to validate (email, phone, IP, IBAN, etc.)
     * @return DymoResponse with risk score and fraud signals, or null if API is
     *         unavailable (no token configured) or the call fails
     */
    public DymoResponse verify(DymoRequest request) {
        // Skip the API call entirely if no token is configured
        if (!StringUtils.hasText(apiToken)) {
            return null;
        }

        // Set required HTTP headers: JSON content type and bearer token auth
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiToken);

        try {
            // POST the request body to the Dymo API — response is raw JSON
            JsonNode rawResponse = restTemplate.postForObject(
                    verifyUrl,
                    new HttpEntity<>(request, headers),
                    JsonNode.class
            );

            // Convert the raw JSON into a typed DymoResponse object
            DymoResponse response = objectMapper.treeToValue(rawResponse, DymoResponse.class);

            // Store the full raw JSON on the response for debugging or downstream use
            response.setRawResponse(rawResponse);

            // Mark that the API was reachable and responded successfully
            response.setApiAvailable(true);

            return response;
        } catch (Exception ex) {
            // If the API call fails (network error, timeout, bad JSON, etc.)
            // return null so callers can fall back gracefully
            // TODO: log the exception and consider returning a fallback DymoResponse
            //       with apiAvailable=false instead of null, to make null checks explicit
            return null;
        }
    }
}
