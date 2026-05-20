package com.frauddetection.servlet;

import com.frauddetection.Alert;
import com.frauddetection.AlertDAO;
import com.frauddetection.FraudEngine;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class DashboardServlet extends HttpServlet {

    private final AlertDAO alertDAO = new AlertDAO();
    private final FraudEngine fraudEngine = new FraudEngine();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Integer> counts = alertDAO.getAlertCounts();
        req.setAttribute("counts", counts);
        req.getRequestDispatcher("/WEB-INF/jsp/dashboard.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("runDetection".equals(action)) {
            try {
                List<List<Alert>> results = List.of(
                    fraudEngine.detectSpendingSpikes(),
                    fraudEngine.detectHighFrequency(),
                    fraudEngine.detectImpossibleTravel(),
                    fraudEngine.detectNewDeviceTransactions(),
                    fraudEngine.detectNightActivity(),
                    fraudEngine.detectFailedLoginClusters()
                );
                
                int count = 0;
                for (List<Alert> alerts : results) {
                    for (Alert a : alerts) {
                        alertDAO.insertAlert(a);
                        count++;
                    }
                }
                req.getSession().setAttribute("message", "Detection cycle complete. Logged " + count + " new alerts.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        resp.sendRedirect("dashboard");
    }
}
