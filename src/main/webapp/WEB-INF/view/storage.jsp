<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<html>
<head>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
    <title>Title</title>
</head>
<body>
<div class="container">
    <div class="row justify-content-center">
        <table class="table">
            <thead>
            <tr>
                <th scope="col">#</th>
                <th scope="col">Название</th>
                <th scope="col">Цена</th>
                <th scope="col">Количество</th>
                <th scope="col">Действие</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="product" items="${products}">
            <tr>
                <td><c:out value="${product.getId()}"></c:out></td>
                <td><c:out value="${product.getName()}"></c:out></td>
                <td><c:out value="${product.getPrice()}"></c:out></td>
                <td><c:out value="${product.getCount()}"></c:out></td>
                <td><a class="btn btn-primary" href="changeProduct?id=<c:out value="${product.getId()}"></c:out>">Изменить</a><br />
                    <form action="storage" method="post">
                        <input style="display: none;" name="productIdToDelete" value='<c:out value="${product.getId()}"></c:out>' />
                        <input type="submit" class="btn btn-primary" value="Удалить" />
                    </form>
                </td>
            </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>

</body>
</html>
