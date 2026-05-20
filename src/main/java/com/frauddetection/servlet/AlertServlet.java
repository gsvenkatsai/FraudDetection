package com.frauddetection.servlet;

import com.frauddetection.Alert;
import com.frauddetection.AlertDAO;
import com.frauddetection.User;
import com.frauddetection.UserDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class AlertServlet extends HttpServlet {

    private final AlertDAO alertDAO = new AlertDAO();
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String severity = req.getParameter("severity");
        if (severity == null || severity.isEmpty()) {
            severity = "All";
        }
        
        List<Alert> alerts = alertDAO.getAlertsBySeverity(severity);
        req.setAttribute("alerts", alerts);
        req.setAttribute("selectedSeverity", severity);

        // Handle Detail View
        String alertIdStr = req.getParameter("id");
        if (alertIdStr != null && !alertIdStr.isEmpty()) {
            try {
                int alertId = Integer.parseInt(alertIdStr);
                Alert selectedAlert = null;
                // Fetch all to find the one (AlertDAO could have a getAlertById, but we use what we have)
                List<Alert> allAlerts = alertDAO.getAlertsBySeverity("All");
                for (Alert a : allAlerts) {
                    if (a.getAlertId() == alertId) {
                        selectedAlert = a;
                        break;
                    }
                }

                if (selectedAlert != null) {
                    req.setAttribute("selectedAlert", selectedAlert);
                    
                    // Fetch User details
                    User alertUser = null;
                    List<User> allUsers = userDAO.getAllUsers();
                    for (User u : allUsers) {
                        if (u.getUserId() == selectedAlert.getUserId()) {
                            alertUser = u;
                            break;
                        }
                    }
                    req.setAttribute("alertUser", alertUser);
                }
            } catch (NumberFormatException e) {
                // Ignore invalid ID
            }
        }

        req.getRequestDispatcher("/WEB-INF/jsp/alerts.jsp").forward(req, resp);
    }
}
