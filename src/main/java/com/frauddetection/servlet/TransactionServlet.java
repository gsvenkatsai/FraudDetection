package com.frauddetection.servlet;

import com.frauddetection.Transaction;
import com.frauddetection.TransactionDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

public class TransactionServlet extends HttpServlet {

    private final TransactionDAO transactionDAO = new TransactionDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Transaction> transactions = transactionDAO.getAllTransactions();
        req.setAttribute("transactions", transactions);
        req.getRequestDispatcher("/WEB-INF/jsp/transactions.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int userId = Integer.parseInt(req.getParameter("userId"));
            BigDecimal amount = new BigDecimal(req.getParameter("amount"));
            String merchant = req.getParameter("merchant");
            String status = req.getParameter("status");

            Transaction newTx = new Transaction(0, userId, amount, new Timestamp(System.currentTimeMillis()), merchant, status);
            transactionDAO.insertTransaction(newTx);
            req.getSession().setAttribute("message", "Transaction for user " + userId + " added successfully.");
        } catch (Exception e) {
            req.getSession().setAttribute("error", "Failed to add transaction: " + e.getMessage());
        }

        resp.sendRedirect("transactions");
    }
}
