package com.example.cscb07_project;

public class User {
    private String userName;
    private String email;
    private String password;
    private String carbonFootprint;

    public User(){}
    public User(String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    public User(String userName, String email, String password, String carbonFootprint) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.carbonFootprint = carbonFootprint;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCarbonFootprint() {
        return carbonFootprint;
    }

    public void setCarbonFootprint(String carbonFootprint) {
        this.carbonFootprint = carbonFootprint;
    }
}