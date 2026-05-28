package com.teamcrocodile.tripwire.model;

public class Account {

    private int id;
    private String email;
    private String ipAddress;
    private String iban;
    private String phoneNumber;


    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getIpAddress() {
        return ipAddress;
    }
    public void setIpAddress(String ip_address) {
        this.ipAddress = ip_address;
    }

    public String getIban() {
        return iban;
    }
    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phone_number) {
        this.phoneNumber = phone_number;
    }


}
