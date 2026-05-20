<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="header.jsp" />

<h2>Transactions History</h2>

<table>
    <thead>
        <tr>
            <th>ID</th>
            <th>User ID</th>
            <th>Amount</th>
            <th>Time</th>
            <th>Merchant</th>
            <th>Status</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="tx" items="${transactions}">
            <tr>
                <td>${tx.transactionId}</td>
                <td>${tx.userId}</td>
                <td>$${tx.amount}</td>
                <td>${tx.transactionTime}</td>
                <td>${tx.merchant}</td>
                <td>${tx.status}</td>
            </tr>
        </c:forEach>
    </tbody>
</table>

<div class="form-section">
    <h3>Add New Transaction</h3>
    <form action="transactions" method="post">
        <div class="form-group">
            <label>User ID:</label>
            <input type="number" name="userId" required>
        </div>
        <div class="form-group">
            <label>Amount:</label>
            <input type="number" step="0.01" name="amount" required>
        </div>
        <div class="form-group">
            <label>Merchant:</label>
            <input type="text" name="merchant">
        </div>
        <div class="form-group">
            <label>Status:</label>
            <select name="status">
                <option value="completed">Completed</option>
                <option value="pending">Pending</option>
                <option value="failed">Failed</option>
            </select>
        </div>
        <button type="submit" class="btn btn-primary">Add Transaction</button>
    </form>
</div>

<jsp:include page="footer.jsp" />
