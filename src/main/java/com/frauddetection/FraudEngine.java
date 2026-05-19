package com.frauddetection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FraudEngine {

    public List<Alert> detectSpendingSpikes() {
        List<Alert> alerts = new ArrayList<>();
        String sql = "WITH UserAvg AS (" +
                     "    SELECT user_id, AVG(amount) as avg_amount " +
                     "    FROM Transactions " +
                     "    WHERE transaction_time >= NOW() - INTERVAL '30 days' " +
                     "    GROUP BY user_id " +
                     ") " +
                     "SELECT DISTINCT t.user_id, CURRENT_TIMESTAMP as alert_time " +
                     "FROM Transactions t " +
                     "JOIN UserAvg ua ON t.user_id = ua.user_id " +
                     "WHERE t.amount > 3 * ua.avg_amount " +
                     "AND NOT EXISTS (SELECT 1 FROM Alerts WHERE user_id = t.user_id AND alert_type = 'SPENDING_SPIKE' AND alert_time >= NOW() - INTERVAL '24 hours')";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                alerts.add(new Alert(0, rs.getInt("user_id"), "SPENDING_SPIKE", 
                        rs.getTimestamp("alert_time"), "high"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return alerts;
    }

    public List<Alert> detectHighFrequency() {
        List<Alert> alerts = new ArrayList<>();
        String sql = "SELECT DISTINCT user_id, CURRENT_TIMESTAMP as alert_time FROM (" +
                     "    SELECT user_id, transaction_time, " +
                     "           COUNT(*) OVER (PARTITION BY user_id ORDER BY transaction_time " +
                     "           RANGE BETWEEN INTERVAL '10 minutes' PRECEDING AND CURRENT ROW) as txn_count " +
                     "    FROM Transactions" +
                     ") t WHERE txn_count >= 5 " +
                     "AND NOT EXISTS (SELECT 1 FROM Alerts WHERE user_id = t.user_id AND alert_type = 'HIGH_FREQUENCY' AND alert_time >= NOW() - INTERVAL '24 hours')";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                alerts.add(new Alert(0, rs.getInt("user_id"), "HIGH_FREQUENCY", 
                        rs.getTimestamp("alert_time"), "medium"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return alerts;
    }

    public List<Alert> detectImpossibleTravel() {
        List<Alert> alerts = new ArrayList<>();
        String sql = "SELECT DISTINCT user_id, CURRENT_TIMESTAMP as alert_time FROM (" +
                     "    SELECT user_id, login_time, country, " +
                     "           LAG(country) OVER (PARTITION BY user_id ORDER BY login_time) as prev_country, " +
                     "           LAG(login_time) OVER (PARTITION BY user_id ORDER BY login_time) as prev_time " +
                     "    FROM Logins" +
                     ") l WHERE country != prev_country AND login_time - prev_time < INTERVAL '1 hour' " +
                     "AND NOT EXISTS (SELECT 1 FROM Alerts WHERE user_id = l.user_id AND alert_type = 'IMPOSSIBLE_TRAVEL' AND alert_time >= NOW() - INTERVAL '24 hours')";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                alerts.add(new Alert(0, rs.getInt("user_id"), "IMPOSSIBLE_TRAVEL", 
                        rs.getTimestamp("alert_time"), "high"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return alerts;
    }

    public List<Alert> detectNewDeviceTransactions() {
        List<Alert> alerts = new ArrayList<>();
        String sql = "SELECT DISTINCT t.user_id, CURRENT_TIMESTAMP as alert_time " +
                     "FROM Transactions t " +
                     "JOIN Logins l ON t.user_id = l.user_id " +
                     "JOIN Devices d ON l.device_id = d.device_id " +
                     "WHERE d.first_seen >= t.transaction_time - INTERVAL '24 hours' " +
                     "AND d.first_seen <= t.transaction_time " +
                     "AND NOT EXISTS (SELECT 1 FROM Alerts WHERE user_id = t.user_id AND alert_type = 'NEW_DEVICE' AND alert_time >= NOW() - INTERVAL '24 hours')";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                alerts.add(new Alert(0, rs.getInt("user_id"), "NEW_DEVICE", 
                        rs.getTimestamp("alert_time"), "low"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return alerts;
    }

    public List<Alert> detectNightActivity() {
        List<Alert> alerts = new ArrayList<>();
        String sql = "SELECT DISTINCT user_id, CURRENT_TIMESTAMP as alert_time FROM Transactions t " +
                     "WHERE EXTRACT(HOUR FROM transaction_time) BETWEEN 1 AND 4 " +
                     "AND NOT EXISTS (SELECT 1 FROM Alerts WHERE user_id = t.user_id AND alert_type = 'NIGHT_ACTIVITY' AND alert_time >= NOW() - INTERVAL '24 hours')";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                alerts.add(new Alert(0, rs.getInt("user_id"), "NIGHT_ACTIVITY", 
                        rs.getTimestamp("alert_time"), "low"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return alerts;
    }

    public List<Alert> detectFailedLoginClusters() {
        List<Alert> alerts = new ArrayList<>();
        String sql = "SELECT DISTINCT user_id, CURRENT_TIMESTAMP as alert_time FROM (" +
                     "    SELECT user_id, login_time, status, " +
                     "           COUNT(*) OVER (PARTITION BY user_id ORDER BY login_time " +
                     "           RANGE BETWEEN INTERVAL '5 minutes' PRECEDING AND CURRENT ROW) as fail_count " +
                     "    FROM Logins WHERE status = 'failed'" +
                     ") l WHERE fail_count >= 3 " +
                     "AND NOT EXISTS (SELECT 1 FROM Alerts WHERE user_id = l.user_id AND alert_type = 'FAILED_LOGIN_CLUSTER' AND alert_time >= NOW() - INTERVAL '24 hours')";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                alerts.add(new Alert(0, rs.getInt("user_id"), "FAILED_LOGIN_CLUSTER", 
                        rs.getTimestamp("alert_time"), "medium"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return alerts;
    }
}
