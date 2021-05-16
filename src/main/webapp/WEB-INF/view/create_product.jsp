<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
    <title>Create product</title>
</head>
<body>
<div class="container">
    <form action="createProduct" method="post">
        <div class="form-group">
            <label>Название</label>
            <input type="text" class="form-control"  name="productToCreateName">
        </div>
        <div class="form-group">
            <label>Цена</label>
            <input type="text" class="form-control" name="productToCreatePrice">
        </div>
        <div class="form-group">
            <label>Количество</label>
            <input type="text" class="form-control" name="productToCreateCount">
        </div>
        <div class="form-group">
            <button class="btn btn-primary" type="submit">Создать</button>
        </div>
    </form>
</div>
</body>
</html>
