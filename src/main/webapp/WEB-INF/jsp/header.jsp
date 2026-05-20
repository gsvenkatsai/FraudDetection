<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Fraud Detection System</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 0; padding: 0; }
        header { background: #333; color: white; padding: 1rem; text-align: center; }
        nav { background: #444; color: white; padding: 0.5rem; text-align: center; }
        nav a { color: white; text-decoration: none; margin: 0 1rem; font-weight: bold; }
        nav a:hover { text-decoration: underline; }
        main { padding: 2rem; }
        .alert-summary { display: flex; gap: 1rem; margin-bottom: 2rem; }
        .card { border: 1px solid #ccc; padding: 1rem; text-align: center; flex: 1; border-radius: 8px; }
        .high { color: red; border-color: red; }
        .medium { color: #c89600; border-color: #c89600; }
        .low { color: #009600; border-color: #009600; }
        table { width: 100%; border-collapse: collapse; margin-top: 1rem; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
        th { background-color: #f2f2f2; }
        tr:nth-child(even) { background-color: #f9f9f9; }
        .form-section { background: #f4f4f4; padding: 1.5rem; border-radius: 8px; margin-top: 2rem; }
        .form-group { margin-bottom: 1rem; }
        .form-group label { display: block; margin-bottom: 0.5rem; }
        .form-group input, .form-group select { width: 100%; padding: 0.5rem; box-sizing: border-box; }
        .btn { padding: 0.7rem 1.5rem; border: none; border-radius: 4px; cursor: pointer; font-size: 1rem; }
        .btn-primary { background: #22014c; color: white; }
        .btn-danger { background: #dc143c; color: white; }
        .message { background: #dff0d8; color: #3c763d; padding: 1rem; border-radius: 4px; margin-bottom: 1rem; }
        .error { background: #f2dede; color: #a94442; padding: 1rem; border-radius: 4px; margin-bottom: 1rem; }
        .detail-view { background: #fffbe6; padding: 1.5rem; border: 1px solid #ffe58f; border-radius: 8px; margin-top: 2rem; }
        pre { background: #eee; padding: 1rem; }
    </style>
</head>
<body>
    <header>
        <h1>Fraud Detection System</h1>
    </header>
    <nav>
        <a href="dashboard">Dashboard</a>
        <a href="users">Users</a>
        <a href="transactions">Transactions</a>
        <a href="alerts">Alerts</a>
    </nav>
    <main>
        <c:if test="${not empty sessionScope.message}">
            <div class="message">${sessionScope.message}</div>
            <c:remove var="message" scope="session" />
        </c:if>
        <c:if test="${not empty sessionScope.error}">
            <div class="error">${sessionScope.error}</div>
            <c:remove var="error" scope="session" />
        </c:if>
