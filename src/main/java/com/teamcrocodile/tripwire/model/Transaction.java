package com.teamcrocodile.tripwire.model;

import java.math.BigDecimal;
import java.util.Date;

public class Transaction {

    private int id;
    private int agentId;
    private int accountId;
    private BigDecimal amount;
    private String currency;
    private Double riskScore;
    private String status;
    private String reasonId;
    private Date createdAt;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getAgentId() {
        return agentId;
    }
    public void setAgentId(int agentId) {
        this.agentId = agentId;
    }

    public int getAccountId() {
        return accountId;
    }
    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Double getRiskScore() {
        return riskScore;
    }
    public void setRiskScore(Double riskScore) {
        this.riskScore = riskScore;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getReasonId() {
        return reasonId;
    }
    public void setReasonId(String reasonId) {
        this.reasonId = reasonId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

}
