package com.teamcrocodile.tripwire.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.teamcrocodile.tripwire.client.DymoClient;
import com.teamcrocodile.tripwire.client.dto.DymoRequest;
import com.teamcrocodile.tripwire.client.dto.DymoResponse;
import com.teamcrocodile.tripwire.client.dto.Rules;
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
        DymoResponse response = dymoClient.verify(DymoRequest.fromAccount(account));
        if (response == null) {
            response = new DymoResponse();
            response.getSignals().add("Dymo API unavailable or token not configured; used local scoring only");
        }

        int apiScore = scoreFromApiResponse(response);
        int localScore = scoreFromLocalAccountSignals(account, response);
        int finalScore = Math.max(apiScore, localScore);

        response.setScore(finalScore);
        response.setFraudulent(finalScore >= 70);
        return response;
    }

    private int scoreFromApiResponse(DymoResponse response) {
        JsonNode node = response.getRawResponse();
        if (node == null || node.isNull()) {
            return 0;
        }

        Double explicitScore = findNumericField(node, Set.of("score", "riskScore", "fraudScore", "risk_score", "fraud_score"));
        if (explicitScore != null) {
            response.getSignals().add("Dymo returned explicit risk score");
            return normalizeScore(explicitScore);
        }

        int score = 0;
        score += scoreRule(response.getEmail() != null && response.getEmail().isFraud(), 20, "Dymo flagged email.fraud", response);
        score += scoreRule(response.getEmail() != null && response.getEmail().isProxiedEmail(), 15, "Dymo flagged email.proxiedEmail", response);
        score += scoreRule(response.getEmail() != null && response.getEmail().isFreeSubdomain(), 10, "Dymo flagged email.freeSubdomain", response);
        score += scoreRule(response.getEmail() != null && response.getEmail().isRoleAccount(), 5, "Dymo flagged email.roleAccount", response);

        score += scoreRule(response.getDomain() != null && response.getDomain().isFraud(), 15, "Dymo flagged domain.fraud", response);

        score += scoreRule(response.getIp() != null && response.getIp().isFraud(), 25, "Dymo flagged ip.fraud", response);
        score += scoreRule(response.getIp() != null && response.getIp().isVpn(), 15, "Dymo flagged ip.vpn", response);
        score += scoreRule(response.getIp() != null && response.getIp().isProxy(), 15, "Dymo flagged ip.proxy", response);

        score += scoreRule(response.getPhone() != null && response.getPhone().isFraud(), 10, "Dymo flagged phone.fraud", response);
        score += scoreRule(response.getIban() != null && response.getIban().isFraud(), 10, "Dymo flagged iban.fraud", response);
        score += scoreRule(isFraud(response.getCreditCard()), 15, "Dymo flagged creditCard.fraud", response);
        score += scoreRule(isFraud(response.getWallet()), 15, "Dymo flagged wallet.fraud", response);
        score += scoreRule(isFraud(response.getUserAgent()), 10, "Dymo flagged userAgent.fraud", response);

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

    private int scoreRule(boolean triggered, int weight, String signal, DymoResponse response) {
        if (!triggered) {
            return 0;
        }
        response.getSignals().add(signal);
        return weight;
    }

    private boolean isFraud(Rules rules) {
        return rules != null && rules.isFraud();
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
