package com.teamcrocodile.tripwire.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EmailRules {
    private boolean valid;
    private boolean fraud;
    private boolean proxiedEmail;
    private boolean freeSubdomain;
    private boolean corporate;
    private String email;
    private String realUser;
    private String didYouMean;
    private boolean noReply;
    private boolean customTLD;
    private String domain;
    private boolean roleAccount;

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public boolean isFraud() {
        return fraud;
    }

    public void setFraud(boolean fraud) {
        this.fraud = fraud;
    }

    public boolean isProxiedEmail() {
        return proxiedEmail;
    }

    public void setProxiedEmail(boolean proxiedEmail) {
        this.proxiedEmail = proxiedEmail;
    }

    public boolean isFreeSubdomain() {
        return freeSubdomain;
    }

    public void setFreeSubdomain(boolean freeSubdomain) {
        this.freeSubdomain = freeSubdomain;
    }

    public boolean isCorporate() {
        return corporate;
    }

    public void setCorporate(boolean corporate) {
        this.corporate = corporate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRealUser() {
        return realUser;
    }

    public void setRealUser(String realUser) {
        this.realUser = realUser;
    }

    public String getDidYouMean() {
        return didYouMean;
    }

    public void setDidYouMean(String didYouMean) {
        this.didYouMean = didYouMean;
    }

    public boolean isNoReply() {
        return noReply;
    }

    public void setNoReply(boolean noReply) {
        this.noReply = noReply;
    }

    public boolean isCustomTLD() {
        return customTLD;
    }

    public void setCustomTLD(boolean customTLD) {
        this.customTLD = customTLD;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public boolean isRoleAccount() {
        return roleAccount;
    }

    public void setRoleAccount(boolean roleAccount) {
        this.roleAccount = roleAccount;
    }
}
