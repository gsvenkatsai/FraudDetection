package com.frauddetection;

import java.sql.Timestamp;

public class Alert {
    private int alertId;
    private int userId;
    private String alertType;
    private Timestamp alertTime;
    private String severity;

    public Alert(int alertId, int userId, String alertType, Timestamp alertTime, String severity) {
        this.alertId = alertId;
        this.userId = userId;
        this.alertType = alertType;
        this.alertTime = alertTime;
        this.severity = severity;
    }

    public int getAlertId() { return alertId; }
    public int getUserId() { return userId; }
    public String getAlertType() { return alertType; }
    public Timestamp getAlertTime() { return alertTime; }
    public String getSeverity() { return severity; }
}
