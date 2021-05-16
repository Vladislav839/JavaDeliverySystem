<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
    <title>Register</title>
</head>
<body>
<%
    String userError = request.getAttribute("UserError") == null ? "" : (String) request.getAttribute("UserError");
    String inputError = request.getAttribute("InputError") == null ? "" : (String) request.getAttribute("InputError");
%>
<div class="container">
<h1><%=userError%></h1>
<h1><%=inputError%></h1>
<div class="form">

    <h1>Регистрация</h1><br>
    <form method="post" action="register">

        <input type="text" required placeholder="login" name="login"><br>
        <input type="password" required placeholder="password" name="password"><br>
        <input type="text" required placeholder="address" name="address"><br><br>
        <input class="button" type="submit" value="Войти">

    </form>
</div>
</div>
</body>
</html>
