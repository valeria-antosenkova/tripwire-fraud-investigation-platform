package com.teamcrocodile.tripwire.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PhoneNumberRules {
    private boolean valid;
    private boolean fraud;
    private String phone;
    private String prefix;
    private String number;
    private String lineType;
    private String country;
    private String countryCode;
    private CarrierInfo carrierInfo;

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getLineType() {
        return lineType;
    }

    public void setLineType(String lineType) {
        this.lineType = lineType;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public CarrierInfo getCarrierInfo() {
        return carrierInfo;
    }

    public void setCarrierInfo(CarrierInfo carrierInfo) {
        this.carrierInfo = carrierInfo;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CarrierInfo {
        private String carrierName;
        private Integer accuracy;
        private String carrierCountry;
        private String carrierCountryCode;

        public String getCarrierName() {
            return carrierName;
        }

        public void setCarrierName(String carrierName) {
            this.carrierName = carrierName;
        }

        public Integer getAccuracy() {
            return accuracy;
        }

        public void setAccuracy(Integer accuracy) {
            this.accuracy = accuracy;
        }

        public String getCarrierCountry() {
            return carrierCountry;
        }

        public void setCarrierCountry(String carrierCountry) {
            this.carrierCountry = carrierCountry;
        }

        public String getCarrierCountryCode() {
            return carrierCountryCode;
        }

        public void setCarrierCountryCode(String carrierCountryCode) {
            this.carrierCountryCode = carrierCountryCode;
        }
    }
}
