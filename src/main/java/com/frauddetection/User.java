package com.frauddetection;

import java.sql.Timestamp;

public class User {
    private int userId;
    private Timestamp signupDate;
    private String country;
    private String status;

    public User(int userId, Timestamp signupDate, String country, String status) {
        this.userId = userId;
        this.signupDate = signupDate;
        this.country = country;
        this.status = status;
    }

    public int getUserId() { return userId; }
    public Timestamp getSignupDate() { return signupDate; }
    public String getCountry() { return country; }
    public String getStatus() { return status; }
}
