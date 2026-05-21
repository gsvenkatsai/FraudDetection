package com.frauddetection;

import java.sql.Timestamp;

public class Login {
    private int loginId;
    private int userId;
    private String userName;
    private Timestamp loginTime;
    private String ipAddress;
    private String country;
    private String deviceId;
    private String status;

    public Login(int loginId, int userId, String userName, Timestamp loginTime, String ipAddress, String country, String deviceId, String status) {
        this.loginId = loginId;
        this.userId = userId;
        this.userName = userName;
        this.loginTime = loginTime;
        this.ipAddress = ipAddress;
        this.country = country;
        this.deviceId = deviceId;
        this.status = status;
    }

    // Getters
    public int getLoginId() { return loginId; }
    public int getUserId() { return userId; }
    public String getUserName() { return userName; }
    public Timestamp getLoginTime() { return loginTime; }
    public String getIpAddress() { return ipAddress; }
    public String getCountry() { return country; }
    public String getDeviceId() { return deviceId; }
    public String getStatus() { return status; }
}
