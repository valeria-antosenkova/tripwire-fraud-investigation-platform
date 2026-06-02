package com.teamcrocodile.tripwire.model;

public enum AgentRole {
    ADMIN("Fraud Detection Manager"),
    ANALYST("Fraud Analyst");

    private final String displayLabel;

    AgentRole(String displayLabel) {
        this.displayLabel = displayLabel;
    }

    public String displayLabel() {
        return displayLabel;
    }

    public static AgentRole fromString(String value) {
        if (value == null || value.isBlank()) {
            return ANALYST;
        }
        return AgentRole.valueOf(value.trim().toUpperCase());
    }
}
