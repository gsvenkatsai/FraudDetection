<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="header.jsp" />

<h2>Users Management</h2>

<table>
    <thead>
        <tr>
            <th>ID</th>
            <th>Full Name</th>
            <th>Country</th>
            <th>Status</th>
            <th>Signup Date</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="user" items="${users}">
            <tr>
                <td>${user.userId}</td>
                <td>${user.fullName}</td>
                <td>${user.country}</td>
                <td>${user.status}</td>
                <td>${user.signupDate}</td>
            </tr>
        </c:forEach>
    </tbody>
</table>

<div class="form-section">
    <h3>Add New User</h3>
    <form action="users" method="post">
        <div class="form-group">
            <label>Full Name:</label>
            <input type="text" name="fullName" required>
        </div>
        <div class="form-group">
            <label>Country:</label>
            <input type="text" name="country">
        </div>
        <div class="form-group">
            <label>Status:</label>
            <select name="status">
                <option value="active">Active</option>
                <option value="suspended">Suspended</option>
                <option value="under_review">Under Review</option>
            </select>
        </div>
        <button type="submit" class="btn btn-primary">Add User</button>
    </form>
</div>

<jsp:include page="footer.jsp" />
