package com.frauddetection;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Transaction {
    private int transactionId;
    private int userId;
    private BigDecimal amount;
    private Timestamp transactionTime;
    private String merchant;
    private String status;

    public Transaction(int transactionId, int userId, BigDecimal amount, Timestamp transactionTime, String merchant, String status) {
        this.transactionId = transactionId;
        this.userId = userId;
        this.amount = amount;
        this.transactionTime = transactionTime;
        this.merchant = merchant;
        this.status = status;
    }

    // Getters
    public int getTransactionId() { return transactionId; }
    public int getUserId() { return userId; }
    public BigDecimal getAmount() { return amount; }
    public Timestamp getTransactionTime() { return transactionTime; }
    public String getMerchant() { return merchant; }
    public String getStatus() { return status; }
}
