<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="header.jsp" />

<h2>Security Alerts</h2>

<div style="margin-bottom: 1rem;">
    <form action="alerts" method="get">
        <label>Filter by Severity: </label>
        <select name="severity">
            <option value="All" ${selectedSeverity == 'All' ? 'selected' : ''}>All</option>
            <option value="High" ${selectedSeverity == 'High' ? 'selected' : ''}>High</option>
            <option value="Medium" ${selectedSeverity == 'Medium' ? 'selected' : ''}>Medium</option>
            <option value="Low" ${selectedSeverity == 'Low' ? 'selected' : ''}>Low</option>
        </select>
        <button type="submit" class="btn btn-primary" style="padding: 0.3rem 1rem;">Filter</button>
    </form>
</div>

<table>
    <thead>
        <tr>
            <th>Alert ID</th>
            <th>User ID</th>
            <th>Severity</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="alert" items="${alerts}">
            <c:set var="rowColor" value="white" />
            <c:set var="sev" value="${alert.severity.toLowerCase()}" />
            <c:choose>
                <c:when test="${sev == 'high'}"><c:set var="rowColor" value="#ffecec" /></c:when>
                <c:when test="${sev == 'medium'}"><c:set var="rowColor" value="#fffce6" /></c:when>
                <c:when test="${sev == 'low'}"><c:set var="rowColor" value="#f6ffed" /></c:when>
            </c:choose>
            <tr style="background-color: ${rowColor};">
                <td><a href="alerts?id=${alert.alertId}&severity=${selectedSeverity}">${alert.alertId}</a></td>
                <td>${alert.userId}</td>
                <td style="text-transform: capitalize;"><strong>${alert.severity}</strong></td>
            </tr>
        </c:forEach>
    </tbody>
</table>

<c:if test="${not empty selectedAlert}">
    <div class="detail-view">
        <div style="display: flex; gap: 2rem;">
            <div style="flex: 1;">
                <h3>Alert Details</h3>
                <p><strong>Type:</strong> ${selectedAlert.alertType}</p>
                <p><strong>Time:</strong> ${selectedAlert.alertTime}</p>
                <p><strong>Severity:</strong> ${selectedAlert.severity}</p>
                <p><strong>Description:</strong></p>
                <div style="background: #fff; padding: 1rem; border: 1px solid #ddd;">
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
                        <p><strong>Signup Date:</strong> ${alertUser.signupDate}</p>
                    </c:when>
                    <c:otherwise>
                        <p>User details not found.</p>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
        <div style="margin-top: 1rem;">
            <a href="alerts?severity=${selectedSeverity}" class="btn btn-primary" style="text-decoration: none;">Close Details</a>
        </div>
    </div>
</c:if>

<jsp:include page="footer.jsp" />
