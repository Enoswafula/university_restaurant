<!-- Member C (Orders & Queue Management) should implement this file -->

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Queue Status</title>
</head>
<body>
    <h2>Your Queue Information</h2>
    <% 
        Integer queueNumber = (Integer) session.getAttribute("queueNumber");
        if (queueNumber != null) {
    %>
        <p>Your queue number is: <strong><%= queueNumber %></strong></p>
        <p>Please wait until your number is called.</p>
    <% } else { %>
        <p>No queue assigned yet. Please place an order first.</p>
    <% } %>
</body>
</html>
