<%@ page import="by.bsu.deliveryshop.data.Models.Customer" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
    <title>USER</title>
</head>
<body>
<%
    String customerName = (String) session.getAttribute("login");
%>
<div class="container">
<h1>Hello <%= customerName %> </h1>
<a href="<c:url value="/logout"/>">Logout</a>
</div>
</body>
</html>
