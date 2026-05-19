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
                     "), " +
                     "Spikes AS (" +
                     "    SELECT t.user_id, t.amount, t.merchant, ua.avg_amount, " +
                     "           ROW_NUMBER() OVER (PARTITION BY t.user_id ORDER BY t.amount DESC) as rn " +
                     "    FROM Transactions t " +
                     "    JOIN UserAvg ua ON t.user_id = ua.user_id " +
                     "    WHERE t.amount > 3 * ua.avg_amount " +
                     ") " +
                     "SELECT user_id, CURRENT_TIMESTAMP as alert_time, " +
                     "       CONCAT('Transaction of $', ROUND(amount, 2), ' at ', merchant, " +
                     "              ' is ', ROUND(amount / NULLIF(avg_amount, 0), 1), " +
                     "              'x the 30-day average ($', ROUND(avg_amount, 2), ')') as description " +
                     "FROM Spikes " +
                     "WHERE rn = 1 " +
                     "AND NOT EXISTS (SELECT 1 FROM Alerts WHERE user_id = Spikes.user_id AND alert_type = 'SPENDING_SPIKE' AND alert_time >= NOW() - INTERVAL '24 hours')";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                alerts.add(new Alert(0, rs.getInt("user_id"), "SPENDING_SPIKE", 
                        rs.getTimestamp("alert_time"), "high", rs.getString("description")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return alerts;
    }

    public List<Alert> detectHighFrequency() {
        List<Alert> alerts = new ArrayList<>();
        String sql = "WITH TxCounts AS (" +
                     "    SELECT user_id, " +
                     "           COUNT(*) OVER (PARTITION BY user_id ORDER BY transaction_time " +
                     "           RANGE BETWEEN INTERVAL '10 minutes' PRECEDING AND CURRENT ROW) as txn_count " +
                     "    FROM Transactions" +
                     "), " +
                     "MaxCounts AS (" +
                     "    SELECT user_id, MAX(txn_count) as max_txn_count " +
                     "    FROM TxCounts " +
                     "    GROUP BY user_id " +
                     ") " +
                     "SELECT user_id, CURRENT_TIMESTAMP as alert_time, " +
                     "       CONCAT(max_txn_count, ' transactions detected within a 10-minute window.') as description " +
                     "FROM MaxCounts " +
                     "WHERE max_txn_count >= 5 " +
                     "AND NOT EXISTS (SELECT 1 FROM Alerts WHERE user_id = MaxCounts.user_id AND alert_type = 'HIGH_FREQUENCY' AND alert_time >= NOW() - INTERVAL '24 hours')";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                alerts.add(new Alert(0, rs.getInt("user_id"), "HIGH_FREQUENCY", 
                        rs.getTimestamp("alert_time"), "medium", rs.getString("description")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return alerts;
    }

    public List<Alert> detectImpossibleTravel() {
        List<Alert> alerts = new ArrayList<>();
        String sql = "WITH Travels AS (" +
                     "    SELECT user_id, login_time, country, " +
                     "           LAG(country) OVER (PARTITION BY user_id ORDER BY login_time) as prev_country, " +
                     "           LAG(login_time) OVER (PARTITION BY user_id ORDER BY login_time) as prev_time " +
                     "    FROM Logins" +
                     "), " +
                     "Violations AS (" +
                     "    SELECT user_id, country, prev_country, " +
                     "           EXTRACT(EPOCH FROM (login_time - prev_time))/60 as diff_minutes, " +
                     "           ROW_NUMBER() OVER (PARTITION BY user_id ORDER BY login_time DESC) as rn " +
                     "    FROM Travels " +
                     "    WHERE country != prev_country AND login_time - prev_time < INTERVAL '1 hour' " +
                     ") " +
                     "SELECT user_id, CURRENT_TIMESTAMP as alert_time, " +
                     "       CONCAT('Impossible travel: logged in from ', prev_country, ' and then ', country, " +
                     "              ' within ', ROUND(diff_minutes, 0), ' minutes.') as description " +
                     "FROM Violations " +
                     "WHERE rn = 1 " +
                     "AND NOT EXISTS (SELECT 1 FROM Alerts WHERE user_id = Violations.user_id AND alert_type = 'IMPOSSIBLE_TRAVEL' AND alert_time >= NOW() - INTERVAL '24 hours')";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                alerts.add(new Alert(0, rs.getInt("user_id"), "IMPOSSIBLE_TRAVEL", 
                        rs.getTimestamp("alert_time"), "high", rs.getString("description")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return alerts;
    }

    public List<Alert> detectNewDeviceTransactions() {
        List<Alert> alerts = new ArrayList<>();
        String sql = "WITH NewDeviceTx AS (" +
                     "    SELECT t.user_id, t.amount, d.device_id, d.device_type, d.os, " +
                     "           ROW_NUMBER() OVER (PARTITION BY t.user_id ORDER BY t.transaction_time DESC) as rn " +
                     "    FROM Transactions t " +
                     "    JOIN Logins l ON t.user_id = l.user_id " +
                     "    JOIN Devices d ON l.device_id = d.device_id " +
                     "    WHERE d.first_seen >= t.transaction_time - INTERVAL '24 hours' " +
                     "      AND d.first_seen <= t.transaction_time " +
                     ") " +
                     "SELECT user_id, CURRENT_TIMESTAMP as alert_time, " +
                     "       CONCAT('Transaction of $', ROUND(amount, 2), ' performed from a newly registered device: ', " +
                     "              device_id, ' (', device_type, ', ', os, ').') as description " +
                     "FROM NewDeviceTx " +
                     "WHERE rn = 1 " +
                     "AND NOT EXISTS (SELECT 1 FROM Alerts WHERE user_id = NewDeviceTx.user_id AND alert_type = 'NEW_DEVICE' AND alert_time >= NOW() - INTERVAL '24 hours')";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                alerts.add(new Alert(0, rs.getInt("user_id"), "NEW_DEVICE", 
                        rs.getTimestamp("alert_time"), "low", rs.getString("description")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return alerts;
    }

    public List<Alert> detectNightActivity() {
        List<Alert> alerts = new ArrayList<>();
        String sql = "WITH NightTx AS (" +
                     "    SELECT t.user_id, t.amount, t.merchant, EXTRACT(HOUR FROM t.transaction_time) as tx_hour, " +
                     "           ROW_NUMBER() OVER (PARTITION BY t.user_id ORDER BY t.transaction_time DESC) as rn " +
                     "    FROM Transactions t " +
                     "    WHERE EXTRACT(HOUR FROM t.transaction_time) BETWEEN 1 AND 4 " +
                     ") " +
                     "SELECT user_id, CURRENT_TIMESTAMP as alert_time, " +
                     "       CONCAT('Transaction of $', ROUND(amount, 2), ' at ', merchant, " +
                     "              ' occurred at ', tx_hour, ':00 AM (high-risk night hours).') as description " +
                     "FROM NightTx " +
                     "WHERE rn = 1 " +
                     "AND NOT EXISTS (SELECT 1 FROM Alerts WHERE user_id = NightTx.user_id AND alert_type = 'NIGHT_ACTIVITY' AND alert_time >= NOW() - INTERVAL '24 hours')";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                alerts.add(new Alert(0, rs.getInt("user_id"), "NIGHT_ACTIVITY", 
                        rs.getTimestamp("alert_time"), "low", rs.getString("description")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return alerts;
    }

    public List<Alert> detectFailedLoginClusters() {
        List<Alert> alerts = new ArrayList<>();
        String sql = "WITH FailCounts AS (" +
                     "    SELECT user_id, " +
                     "           COUNT(*) OVER (PARTITION BY user_id ORDER BY login_time " +
                     "           RANGE BETWEEN INTERVAL '5 minutes' PRECEDING AND CURRENT ROW) as fail_count " +
                     "    FROM Logins WHERE status = 'failed'" +
                     "), " +
                     "MaxFails AS (" +
                     "    SELECT user_id, MAX(fail_count) as max_fail_count " +
                     "    FROM FailCounts " +
                     "    GROUP BY user_id " +
                     ") " +
                     "SELECT user_id, CURRENT_TIMESTAMP as alert_time, " +
                     "       CONCAT(max_fail_count, ' failed login attempts within a 5-minute window.') as description " +
                     "FROM MaxFails " +
                     "WHERE max_fail_count >= 3 " +
                     "AND NOT EXISTS (SELECT 1 FROM Alerts WHERE user_id = MaxFails.user_id AND alert_type = 'FAILED_LOGIN_CLUSTER' AND alert_time >= NOW() - INTERVAL '24 hours')";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                alerts.add(new Alert(0, rs.getInt("user_id"), "FAILED_LOGIN_CLUSTER", 
                        rs.getTimestamp("alert_time"), "medium", rs.getString("description")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return alerts;
    }
}
