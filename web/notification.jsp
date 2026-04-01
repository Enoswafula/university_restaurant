<!-- Member D (Payments & Notifications) should implement this file -->

<%@ page import="java.util.List, model.Notification, model.dao.NotificationDAO, model.User" %>
<%
    User user = (User) session.getAttribute("user");
    NotificationDAO dao = new NotificationDAO();
    List<Notification> notifications = dao.getNotificationsByUser(user.getUserId());
%>
<html>
<head>
    <title>Notifications</title>
</head>
<body>
    <h2>Your Notifications</h2>
    <ul>
        <% for(Notification n : notifications) { %>
            <li>
                <strong><%= n.getType() %></strong>: <%= n.getMessage() %> 
                (Sent at: <%= n.getSentAt() %>)
            </li>
        <% } %>
    </ul>
</body>
</html>
