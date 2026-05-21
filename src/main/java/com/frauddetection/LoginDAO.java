package com.frauddetection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LoginDAO {
    public List<Login> getAllLogins() {
        List<Login> logins = new ArrayList<>();
        String sql = "SELECT l.*, u.full_name, d.device_type, d.os " +
                     "FROM Logins l " +
                     "JOIN Users u ON l.user_id = u.user_id " +
                     "LEFT JOIN Devices d ON l.device_id = d.device_id " +
                     "ORDER BY l.login_time DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                logins.add(new Login(
                    rs.getInt("login_id"),
                    rs.getInt("user_id"),
                    rs.getString("full_name"),
                    rs.getTimestamp("login_time"),
                    rs.getString("ip_address"),
                    rs.getString("country"),
                    rs.getString("device_id"),
                    rs.getString("device_type"),
                    rs.getString("os"),
                    rs.getString("status")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return logins;
    }
}
