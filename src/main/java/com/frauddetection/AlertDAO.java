package com.frauddetection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlertDAO {
    public List<Alert> getAllAlerts() {
        return getAlertsBySeverity("All");
    }

    public List<Alert> getAlertsBySeverity(String severity) {
        List<Alert> alerts = new ArrayList<>();
        String query = "SELECT * FROM Alerts";
        if (!"All".equalsIgnoreCase(severity)) {
            query += " WHERE severity = ?";
        }
        query += " ORDER BY alert_time DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            if (!"All".equalsIgnoreCase(severity)) {
                pstmt.setString(1, severity.toLowerCase());
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    alerts.add(new Alert(
                            rs.getInt("alert_id"),
                            rs.getInt("user_id"),
                            rs.getString("alert_type"),
                            rs.getTimestamp("alert_time"),
                            rs.getString("severity")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return alerts;
    }

    public java.util.Map<String, Integer> getAlertCounts() {
        java.util.Map<String, Integer> counts = new java.util.HashMap<>();
        counts.put("high", 0);
        counts.put("medium", 0);
        counts.put("low", 0);

        String query = "SELECT severity, COUNT(*) as count FROM Alerts GROUP BY severity";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                counts.put(rs.getString("severity").toLowerCase(), rs.getInt("count"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return counts;
    }

    public void insertAlert(Alert alert) {
        String query = "INSERT INTO Alerts (user_id, alert_type, alert_time, severity) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, alert.getUserId());
            pstmt.setString(2, alert.getAlertType());
            pstmt.setTimestamp(3, alert.getAlertTime());
            pstmt.setString(4, alert.getSeverity());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
