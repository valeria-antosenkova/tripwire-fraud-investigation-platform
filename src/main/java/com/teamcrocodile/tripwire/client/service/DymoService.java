package com.teamcrocodile.tripwire.client.service;

import org.springframework.stereotype.Service;

@Service
public class DymoService {

    /*private final RestTemplate restTemplate = new RestTemplate();

    private static final String API_KEY = "YOUR_API_KEY";

    public double checkAccount(Account account) {

        HttpHeaders headers = new HttpHeaders();

        headers.setBearerAuth(API_KEY);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();

        body.put("email", account.getEmail());
        body.put("phone", account.getPhoneNumber());
        body.put("ip", account.getIpAddress());
        body.put("iban", account.getIban());

        HttpEntity<Map<String, Object>> request =
                new HttpEntity<>(body, headers);

        ResponseEntity<String> response =
                restTemplate.postForEntity(
                        "https://api.dymo.ai/validate",
                        request,
                        String.class
                );

        System.out.println(response.getBody());

        // temporary
        return 85.0;
    }*/
}
