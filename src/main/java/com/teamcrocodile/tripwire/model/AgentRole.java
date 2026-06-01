package com.teamcrocodile.tripwire.model;

public enum AgentRole {
    ADMIN,
    ANALYST;

    public String displayLabel() {
        return this == ADMIN ? "Fraud Detection Manager" : "Fraud Analyst";
    }

    public static AgentRole fromString(String value) {
        if (value == null || value.isBlank()) {
            return ANALYST;
        }
        return AgentRole.valueOf(value.trim().toUpperCase());
    }
}
