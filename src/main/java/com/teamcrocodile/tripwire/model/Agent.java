package com.teamcrocodile.tripwire.model;

public class Agent {

    private int id;
    private String name;
    private String email;
    private String pass_hash;
    private String profile_picture;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass_hash() {
        return pass_hash;
    }
    public void setPass_hash(String pass_hash) {
        this.pass_hash = pass_hash;
    }

    public String getProfile_picture() {
        return profile_picture;
    }
    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

}
