package com.frauddetection;

import java.sql.Timestamp;

public class Alert {
    private int alertId;
    private int userId;
    private String alertType;
    private Timestamp alertTime;
    private String severity;
    private String description;

    public Alert(int alertId, int userId, String alertType, Timestamp alertTime, String severity, String description) {
        this.alertId = alertId;
        this.userId = userId;
        this.alertType = alertType;
        this.alertTime = alertTime;
        this.severity = severity;
        this.description = description;
    }

    public int getAlertId() { return alertId; }
    public int getUserId() { return userId; }
    public String getAlertType() { return alertType; }
    public Timestamp getAlertTime() { return alertTime; }
    public String getSeverity() { return severity; }
    public String getDescription() { return description; }
}
