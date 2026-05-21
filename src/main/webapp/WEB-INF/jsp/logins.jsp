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
                <th>Location</th>
                <th>IP Address</th>
                <th>Device</th>
                <th>Type / OS</th>
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
                        <c:choose>
                            <c:when test="${not empty login.deviceType}">
                                ${login.deviceType} / ${login.os}
                            </c:when>
                            <c:otherwise>
                                <span class="text-muted">Unknown</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
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
