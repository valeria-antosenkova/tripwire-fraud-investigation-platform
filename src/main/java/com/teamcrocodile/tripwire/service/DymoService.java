package com.teamcrocodile.tripwire.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.teamcrocodile.tripwire.client.DymoClient;
import com.teamcrocodile.tripwire.client.dto.DymoRequest;
import com.teamcrocodile.tripwire.client.dto.DymoResponse;
import com.teamcrocodile.tripwire.model.Account;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

@Service
public class DymoService {

    private static final Set<String> DISPOSABLE_EMAIL_DOMAINS = Set.of(
            "guerrillamail.com",
            "mailinator.com",
            "10minutemail.com",
            "tempmail.com",
            "yopmail.com"
    );

    private final DymoClient dymoClient;

    public DymoService(DymoClient dymoClient) {
        this.dymoClient = dymoClient;
    }

    public DymoResponse checkAccount(Account account) {
        DymoResponse response = new DymoResponse();
        JsonNode rawResponse = dymoClient.verify(DymoRequest.fromAccount(account));
        response.setRawResponse(rawResponse);
        response.setApiAvailable(rawResponse != null);

        int apiScore = scoreFromApiResponse(rawResponse, response);
        int localScore = scoreFromLocalAccountSignals(account, response);
        int finalScore = Math.max(apiScore, localScore);

        response.setScore(finalScore);
        response.setFraudulent(finalScore >= 70);
        return response;
    }

    private int scoreFromApiResponse(JsonNode node, DymoResponse response) {
        if (node == null || node.isNull()) {
            response.getSignals().add("Dymo API unavailable or token not configured; used local scoring only");
            return 0;
        }

        Double explicitScore = findNumericField(node, Set.of("score", "riskScore", "fraudScore", "risk_score", "fraud_score"));
        if (explicitScore != null) {
            response.getSignals().add("Dymo returned explicit risk score");
            return normalizeScore(explicitScore);
        }

        int score = 0;
        score += scoreBooleanFlags(node, response, "");
        return Math.min(score, 100);
    }

    private Double findNumericField(JsonNode node, Set<String> fieldNames) {
        if (node == null) {
            return null;
        }

        if (node.isObject()) {
            Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();
                if (fieldNames.contains(field.getKey()) && field.getValue().isNumber()) {
                    return field.getValue().asDouble();
                }
                Double nested = findNumericField(field.getValue(), fieldNames);
                if (nested != null) {
                    return nested;
                }
            }
        }

        if (node.isArray()) {
            for (JsonNode item : node) {
                Double nested = findNumericField(item, fieldNames);
                if (nested != null) {
                    return nested;
                }
            }
        }

        return null;
    }

    private int scoreBooleanFlags(JsonNode node, DymoResponse response, String path) {
        int score = 0;

        if (node.isObject()) {
            Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();
                String key = field.getKey().toLowerCase(Locale.ROOT);
                String currentPath = path.isBlank() ? field.getKey() : path + "." + field.getKey();
                JsonNode value = field.getValue();

                if (value.isBoolean() && value.asBoolean()) {
                    int weight = apiFlagWeight(currentPath, key);
                    if (weight > 0) {
                        score += weight;
                        response.getSignals().add("Dymo flagged " + currentPath);
                    }
                }

                score += scoreBooleanFlags(value, response, currentPath);
            }
        } else if (node.isArray()) {
            for (JsonNode item : node) {
                score += scoreBooleanFlags(item, response, path);
            }
        }

        return Math.min(score, 100);
    }

    private int apiFlagWeight(String path, String key) {
        String normalizedPath = path.toLowerCase(Locale.ROOT);

        if (key.contains("fraud")) {
            if (normalizedPath.startsWith("ip.")) {
                return 25;
            }
            if (normalizedPath.startsWith("email.")) {
                return 20;
            }
            if (normalizedPath.startsWith("domain.")) {
                return 15;
            }
            if (normalizedPath.startsWith("creditcard.") || normalizedPath.startsWith("wallet.")) {
                return 15;
            }
            if (normalizedPath.startsWith("phone.") || normalizedPath.startsWith("iban.")) {
                return 10;
            }
            return 15;
        }

        if (key.contains("blacklist") || key.contains("tor")) {
            return 25;
        }
        if (key.contains("proxy") || key.contains("vpn")) {
            return 15;
        }
        if (key.contains("disposable") || key.contains("proxiedemail")) {
            return 15;
        }
        if (key.contains("risk") || key.contains("suspicious")) {
            return 10;
        }

        return 0;
    }

    private int scoreFromLocalAccountSignals(Account account, DymoResponse response) {
        int score = 0;

        String emailDomain = domainFromEmail(account.getEmail());
        if (DISPOSABLE_EMAIL_DOMAINS.contains(emailDomain)) {
            score += 15;
            response.getSignals().add("Disposable email domain: " + emailDomain);
        }

        String phoneCountry = countryFromPhone(account.getPhoneNumber());
        String ibanCountry = countryFromIban(account.getIban());
        if (phoneCountry != null && ibanCountry != null && !phoneCountry.equals(ibanCountry)) {
            score += 10;
            response.getSignals().add("Phone country does not match IBAN country");
        }

        Long ageHours = accountAgeHours(account.getCreatedAt());
        if (ageHours != null && ageHours <= 24) {
            score += 15;
            response.getSignals().add("Account is less than 24 hours old");
        } else if (ageHours != null && ageHours <= 7 * 24) {
            score += 10;
            response.getSignals().add("Account is less than 7 days old");
        }

        return Math.min(score, 100);
    }

    private int normalizeScore(double score) {
        if (score <= 1.0) {
            return (int) Math.round(score * 100);
        }
        return (int) Math.round(Math.max(0, Math.min(score, 100)));
    }

    private String domainFromEmail(String email) {
        if (email == null || !email.contains("@")) {
            return null;
        }
        return email.substring(email.indexOf('@') + 1).toLowerCase(Locale.ROOT);
    }

    private String countryFromIban(String iban) {
        if (iban == null || iban.length() < 2) {
            return null;
        }
        return iban.substring(0, 2).toUpperCase(Locale.ROOT);
    }

    private String countryFromPhone(String phoneNumber) {
        if (phoneNumber == null) {
            return null;
        }
        if (phoneNumber.startsWith("+34")) {
            return "ES";
        }
        if (phoneNumber.startsWith("+44")) {
            return "GB";
        }
        if (phoneNumber.startsWith("+49")) {
            return "DE";
        }
        if (phoneNumber.startsWith("+55")) {
            return "BR";
        }
        if (phoneNumber.startsWith("+65")) {
            return "SG";
        }
        if (phoneNumber.startsWith("+7")) {
            return "RU";
        }
        if (phoneNumber.startsWith("+1")) {
            return "US";
        }
        return null;
    }

    private Long accountAgeHours(String createdAt) {
        if (createdAt == null) {
            return null;
        }

        try {
            LocalDateTime created = LocalDateTime.parse(createdAt.replace(' ', 'T'));
            return Duration.between(created, LocalDateTime.now()).toHours();
        } catch (RuntimeException ex) {
            try {
                LocalDateTime created = LocalDateTime.parse(createdAt, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                return Duration.between(created, LocalDateTime.now()).toHours();
            } catch (RuntimeException ignored) {
                return null;
            }
        }
    }
}
