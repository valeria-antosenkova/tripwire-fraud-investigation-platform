package com.teamcrocodile.tripwire.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DymoResponse {

    private boolean apiAvailable;
    private boolean fraudulent;
    private double score;
    private List<String> signals = new ArrayList<>();
    private JsonNode rawResponse;
    private Rules url;
    private EmailRules email;
    private PhoneNumberRules phone;
    private Rules domain;
    private Rules creditCard;
    private IpRules ip;
    private Rules wallet;
    private Rules userAgent;
    private IbanRules iban;

    public boolean isApiAvailable() {
        return apiAvailable;
    }

    public void setApiAvailable(boolean apiAvailable) {
        this.apiAvailable = apiAvailable;
    }

    public boolean isFraudulent() {
        return fraudulent;
    }

    public void setFraudulent(boolean fraudulent) {
        this.fraudulent = fraudulent;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public List<String> getSignals() {
        return signals;
    }

    public void setSignals(List<String> signals) {
        this.signals = signals;
    }

    public JsonNode getRawResponse() {
        return rawResponse;
    }

    public void setRawResponse(JsonNode rawResponse) {
        this.rawResponse = rawResponse;
    }

    public Rules getUrl() {
        return url;
    }

    public void setUrl(Rules url) {
        this.url = url;
    }

    public EmailRules getEmail() {
        return email;
    }

    public void setEmail(EmailRules email) {
        this.email = email;
    }

    public PhoneNumberRules getPhone() {
        return phone;
    }

    public void setPhone(PhoneNumberRules phone) {
        this.phone = phone;
    }

    public Rules getDomain() {
        return domain;
    }

    public void setDomain(Rules domain) {
        this.domain = domain;
    }

    public Rules getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(Rules creditCard) {
        this.creditCard = creditCard;
    }

    public IpRules getIp() {
        return ip;
    }

    public void setIp(IpRules ip) {
        this.ip = ip;
    }

    public Rules getWallet() {
        return wallet;
    }

    public void setWallet(Rules wallet) {
        this.wallet = wallet;
    }

    public Rules getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(Rules userAgent) {
        this.userAgent = userAgent;
    }

    public IbanRules getIban() {
        return iban;
    }

    public void setIban(IbanRules iban) {
        this.iban = iban;
    }
}
