package com.teamcrocodile.tripwire.client.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.teamcrocodile.tripwire.model.Account;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DymoRequest {

    private String url;
    private String email;
    private String phone;
    private String domain;
    private String ip;
    private String wallet;
    private String userAgent;
    private String iban;
    private CreditCard creditCard;

    public static DymoRequest fromAccount(Account account) {
        DymoRequest request = new DymoRequest();
        request.setEmail(account.getEmail());
        request.setPhone(account.getPhoneNumber());
        request.setIp(account.getIpAddress());
        request.setIban(account.getIban());
        request.setDomain(extractDomain(account.getEmail()));
        return request;
    }

    private static String extractDomain(String email) {
        if (email == null || !email.contains("@")) {
            return null;
        }
        return email.substring(email.indexOf('@') + 1).toLowerCase();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getWallet() {
        return wallet;
    }

    public void setWallet(String wallet) {
        this.wallet = wallet;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class CreditCard {
        private String pan;
        private String expirationDate;
        private String cvv;

        public String getPan() {
            return pan;
        }

        public void setPan(String pan) {
            this.pan = pan;
        }

        public String getExpirationDate() {
            return expirationDate;
        }

        public void setExpirationDate(String expirationDate) {
            this.expirationDate = expirationDate;
        }

        public String getCvv() {
            return cvv;
        }

        public void setCvv(String cvv) {
            this.cvv = cvv;
        }
    }
}
