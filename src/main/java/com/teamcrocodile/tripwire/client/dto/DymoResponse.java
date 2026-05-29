package com.teamcrocodile.tripwire.client.dto;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;

public class DymoResponse {

    private boolean apiAvailable;
    private boolean fraudulent;
    private double score;
    private List<String> signals = new ArrayList<>();
    private JsonNode rawResponse;

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
}
