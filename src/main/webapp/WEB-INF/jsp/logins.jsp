<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<jsp:include page="header.jsp" />

<h2>Login History</h2>

<div class="table-container">
    <table class="data-table">
        <thead>
            <tr>
                <th>ID</th>
                <th>User</th>
                <th>Time</th>
                <th>Country</th>
                <th>IP Address</th>
                <th>Device ID</th>
                <th>Status</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="login" items="${logins}">
                <tr>
                    <td>${login.loginId}</td>
                    <td>${login.userName} (ID: ${login.userId})</td>
                    <td><fmt:formatDate value="${login.loginTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
                    <td>${login.country}</td>
                    <td>${login.ipAddress}</td>
                    <td><code>${login.deviceId}</code></td>
                    <td>
                        <span class="badge ${login.status == 'success' ? 'bg-success' : 'bg-danger'}">
                            ${login.status}
                        </span>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>

<jsp:include page="footer.jsp" />
