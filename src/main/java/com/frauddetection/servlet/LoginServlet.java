package com.frauddetection.servlet;

import com.frauddetection.Login;
import com.frauddetection.LoginDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class LoginServlet extends HttpServlet {
    private final LoginDAO loginDAO = new LoginDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Login> logins = loginDAO.getAllLogins();
        req.setAttribute("logins", logins);
        req.getRequestDispatcher("/WEB-INF/jsp/logins.jsp").forward(req, resp);
    }
}
