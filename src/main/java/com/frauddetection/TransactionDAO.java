package com.frauddetection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {
    public List<Transaction> getAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        String query = "SELECT * FROM Transactions ORDER BY transaction_time DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                transactions.add(new Transaction(
                        rs.getInt("transaction_id"),
                        rs.getInt("user_id"),
                        rs.getBigDecimal("amount"),
                        rs.getTimestamp("transaction_time"),
                        rs.getString("merchant"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    public void insertTransaction(Transaction transaction) {
        String query = "INSERT INTO Transactions (user_id, amount, transaction_time, merchant, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, transaction.getUserId());
            pstmt.setBigDecimal(2, transaction.getAmount());
            pstmt.setTimestamp(3, transaction.getTransactionTime());
            pstmt.setString(4, transaction.getMerchant());
            pstmt.setString(5, transaction.getStatus());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
