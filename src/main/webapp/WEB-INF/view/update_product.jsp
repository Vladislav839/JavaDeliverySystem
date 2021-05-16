<%@ page import="by.bsu.deliveryshop.data.Models.Product" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
    <title>Title</title>
</head>
<body>
<%
    Product product = (Product) request.getAttribute("updateProduct");
%>
<div class="container">
<form action="changeProduct" method="post">
    <div style="display: none;" class="form-group">
        <label>Id</label>
        <input type="text" class="form-control"  name="productToChangeId" readonly value="<%= product.getId() %>">
    </div>
    <div class="form-group">
        <label>Название</label>
        <input type="text" class="form-control"  name="productToChangeName"value="<%= product.getName() %>">
    </div>
    <div class="form-group">
        <label>Цена</label>
        <input type="text" class="form-control" name="productToChangePrice" value="<%= String.valueOf(product.getPrice()) %>">
    </div>
    <div class="form-group">
        <label>Количество</label>
        <input type="text" class="form-control" name="productToChangeCount" value="<%= String.valueOf(product.getCount()) %>">
    </div>
    <div class="form-group">
        <button class="btn btn-primary" type="submit">Изменить</button>
    </div>
</form>
</div>
</body>
</html>
