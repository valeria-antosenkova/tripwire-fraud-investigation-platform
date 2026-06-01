package com.teamcrocodile.tripwire.model;

import java.math.BigDecimal;
import java.util.Date;

public class Transaction {

    private int id;
    private Integer agentId;
    private int accountId;
    private BigDecimal amount;
    private String currency;
    private Double riskScore;
    private Status status;
    private String reasonText;
    private String agentNote;
    private Date createdAt;

    // Order details submitted by the customer
    private String orderId;
    private String orderDate;
    private String items;          // JSON array of item names
    private String paymentMethod;
    private String shippingAddress;
    private String billingAddress;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Integer getAgentId() { return agentId; }
    public void setAgentId(Integer agentId) { this.agentId = agentId; }

    public int getAccountId() { return accountId; }
    public void setAccountId(int accountId) { this.accountId = accountId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public Double getRiskScore() { return riskScore; }
    public void setRiskScore(Double riskScore) { this.riskScore = riskScore; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public String getReasonText() { return reasonText; }
    public void setReasonText(String reasonText) { this.reasonText = reasonText; }

    public String getAgentNote() { return agentNote; }
    public void setAgentNote(String agentNote) { this.agentNote = agentNote; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getOrderDate() { return orderDate; }
    public void setOrderDate(String orderDate) { this.orderDate = orderDate; }

    public String getItems() { return items; }
    public void setItems(String items) { this.items = items; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }

    public String getBillingAddress() { return billingAddress; }
    public void setBillingAddress(String billingAddress) { this.billingAddress = billingAddress; }

}
