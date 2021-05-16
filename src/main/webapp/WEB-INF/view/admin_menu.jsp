<%@ page import="by.bsu.deliveryshop.data.Models.Seller" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
    <title>ADMIN</title>
</head>
<body>
<%
    String sellerName = (String) session.getAttribute("login");
%>
<div class="container">
<h1>Hello <%= sellerName %> </h1>
<a href="<c:url value='/storage' />">Склад</a><br />
<a href="<c:url value='/createProduct' />">Добавить товар на склад</a><br />
<a href="<c:url value='/logout' />">Logout</a>
</div>
</body>
</html>
