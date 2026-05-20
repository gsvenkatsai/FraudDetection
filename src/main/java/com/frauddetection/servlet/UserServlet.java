package com.frauddetection.servlet;

import com.frauddetection.User;
import com.frauddetection.UserDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

public class UserServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<User> users = userDAO.getAllUsers();
        req.setAttribute("users", users);
        req.getRequestDispatcher("/WEB-INF/jsp/users.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fullName = req.getParameter("fullName");
        String country = req.getParameter("country");
        String status = req.getParameter("status");

        if (fullName != null && !fullName.isEmpty()) {
            User newUser = new User(0, fullName, new Timestamp(System.currentTimeMillis()), country, status);
            userDAO.insertUser(newUser);
            req.getSession().setAttribute("message", "User " + fullName + " added successfully.");
        }

        resp.sendRedirect("users");
    }
}
