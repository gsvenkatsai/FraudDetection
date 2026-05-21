<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<jsp:include page="header.jsp" />

<h2>Security Alerts</h2>

<div style="margin-bottom: 1rem;">
    <form action="alerts" method="get">
        <label>Filter by Severity: </label>
        <select name="severity">
            <c:set var="severities" value="${['All', 'High', 'Medium', 'Low']}" />
            <c:forEach var="sevOpt" items="${severities}">
                <option value="${sevOpt}" ${selectedSeverity == sevOpt ? 'selected' : ''}>${sevOpt}</option>
            </c:forEach>
        </select>
        <button type="submit" class="btn btn-primary" style="padding: 0.3rem 1rem;">Filter</button>
    </form>
</div>

<div class="table-container">
    <table class="data-table">
        <thead>
            <tr>
                <th>Alert ID</th>
                <th>User ID</th>
                <th>Time</th>
                <th>Severity</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="alert" items="${alerts}">
                <c:set var="sevClass" value="severity-${alert.severity != null ? alert.severity.toLowerCase() : 'none'}" />
                
                <tr class="${sevClass}">
                    <td><a href="alerts?id=${alert.alertId}&severity=${selectedSeverity}">${alert.alertId}</a></td>
                    <td>${alert.userId}</td>
                    <td><fmt:formatDate value="${alert.alertTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
                    <td style="text-transform: capitalize;"><strong>${alert.severity}</strong></td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>

<c:if test="${not empty selectedAlert}">
    <div class="detail-view">
        <div style="display: flex; gap: 2rem;">
            <div style="flex: 1;">
                <h3>Alert Details</h3>
                <p><strong>Type:</strong> ${selectedAlert.alertType}</p>
                <p><strong>Time:</strong> <fmt:formatDate value="${selectedAlert.alertTime}" pattern="yyyy-MM-dd HH:mm:ss" /></p>
                <p><strong>Severity:</strong> <span style="text-transform: capitalize;">${selectedAlert.severity}</span></p>
                <p><strong>Description:</strong></p>
                <div style="background: #fff; padding: 1rem; border: 1px solid #ddd; border-radius: 4px;">
                    ${selectedAlert.description}
                </div>
            </div>
            <div style="flex: 1;">
                <h3>User Information</h3>
                <c:choose>
                    <c:when test="${not empty alertUser}">
                        <p><strong>Full Name:</strong> ${alertUser.fullName}</p>
                        <p><strong>User ID:</strong> ${alertUser.userId}</p>
                        <p><strong>Country:</strong> ${alertUser.country}</p>
                        <p><strong>Status:</strong> ${alertUser.status}</p>
                        <p><strong>Signup Date:</strong> <fmt:formatDate value="${alertUser.signupDate}" pattern="yyyy-MM-dd" /></p>
                    </c:when>
                    <c:otherwise>
                        <p>User details not found.</p>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
        <div style="margin-top: 1.5rem;">
            <a href="alerts?severity=${selectedSeverity}" class="btn btn-primary" style="text-decoration: none;">Close Details</a>
        </div>
    </div>
</c:if>

<jsp:include page="footer.jsp" />
