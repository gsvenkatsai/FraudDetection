package com.frauddetection;

import java.sql.Timestamp;

public class User {
    private int userId;
    private String fullName;
    private Timestamp signupDate;
    private String country;
    private String status;

    public User(int userId, String fullName, Timestamp signupDate, String country, String status) {
        this.userId = userId;
        this.fullName = fullName;
        this.signupDate = signupDate;
        this.country = country;
        this.status = status;
    }

    public int getUserId() { return userId; }
    public String getFullName() { return fullName; }
    public Timestamp getSignupDate() { return signupDate; }
    public String getCountry() { return country; }
    public String getStatus() { return status; }
}
